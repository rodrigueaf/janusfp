package com.gt.gestfinance.service.impl;

import com.gt.gestfinance.entity.*;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.repository.CompteRepository;
import com.gt.gestfinance.repository.OperationDetailRepository;
import com.gt.gestfinance.repository.TresorerieRepository;
import com.gt.gestfinance.repository.VersionRepository;
import com.gt.gestfinance.service.ICompteService;
import com.gt.gestfinance.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe Service de l'entité Compte
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 31/03/2018
 */
@Service
public class CompteService extends BaseEntityService<Compte, Integer> implements ICompteService {

    private OperationDetailRepository operationDetailRepository;
    private VersionRepository versionRepository;
    private TresorerieRepository tresorerieRepository;

    @Autowired
    public CompteService(CompteRepository repository) {
        super(repository);
    }

    @Override
    public VersionRepository getVersionRepository() {
        return versionRepository;
    }

    @Override
    public synchronized Compte save(Compte compte) throws CustomException {
        controlerLIntegriteDuComtpe(compte);
        reconstructionDuCmpte(compte);
        compte = super.save(compte);
        miseAJourDeLaVersion(compte, Version.MOTIF_AJOUT, compte.getIdentifiant());
        return compte;
    }

    private void controlerLIntegriteDuComtpe(Compte compte) throws CustomException {
        if (compte.getIdentifiant() != null && compte.getUtilisable()) {
            Optional<Compte> o = ((CompteRepository) repository)
                    .findWithSousComptesByIdentifiant(compte.getIdentifiant());
            if (o.isPresent() && !o.get().getSousComptes().isEmpty())
                throw new CustomException(MPConstants.LE_COMPTE_EST_DEJA_PARENT);
        }
        if (compte.getCompteParent() != null && compte.getCompteParent().getUtilisable())
            throw new CustomException(MPConstants.LE_COMPTE_PARENT_EST_DEJA_UTILISABLR);
        if (compte.getLibelle() == null || compte.getLibelle().isEmpty())
            throw new CustomException(MPConstants.LE_LIBELLE_EST_OBLIGATOIRE);
        Optional<Compte> o = ((CompteRepository) repository).findByLibelle(compte.getLibelle());
        if (o.isPresent() && !o.get().getIdentifiant().equals(compte.getIdentifiant()))
            throw new CustomException(MPConstants.LE_LIBELLE_EXISTE_DEJA);
    }

    private void reconstructionDuCmpte(Compte compte) {
        if (compte.getCompteParent() != null && compte.getCompteParent().getIdentifiant() != null) {
            compte.setCompteParent(findOne(compte.getCompteParent().getIdentifiant()));
        }
    }

    @Override
    public synchronized Compte saveAndFlush(Compte compte) throws CustomException {
        controlerLIntegriteDuComtpe(compte);
        reconstructionDuCmpte(compte);
        compte = super.saveAndFlush(compte);
        miseAJourDeLaVersion(compte, Version.MOTIF_MODIFICATION, compte.getIdentifiant());
        return compte;
    }

    @Override
    public boolean supprimer(Integer compteId) throws CustomException {
        Optional<Compte> o = ((CompteRepository) repository)
                .findWithSousComptesByIdentifiant(compteId);
        if (o.isPresent() && !o.get().getSousComptes().isEmpty())
            throw new CustomException(MPConstants.LE_COMPTE_A_DEJA_SOUS_COMPTE);
        if (!operationDetailRepository.findByCompteIdentifiant(compteId).isEmpty())
            throw new CustomException(MPConstants.LE_COMPTE_DEJA_UTILISER);
        if (super.delete(compteId)) {
            miseAJourDeLaVersion(o.orElseThrow(CustomException::new), Version.MOTIF_SUPPRESSION, compteId);
            return true;
        }
        return false;
    }

    @Override
    public boolean supprimerForcer(Integer compteId) {
        operationDetailRepository.deleteByCompteIdentifiant(compteId);
        List<Tresorerie> tresoreries = tresorerieRepository
                .findByCompteLogiqueIdentifiantOrComptePhysiqueIdentifiant(compteId, compteId);
        for (Tresorerie tresorerie : tresoreries) {
            List<OperationDetail> operationDetails = operationDetailRepository
                    .findByTresorerieIdentifiant(tresorerie.getIdentifiant());
            for (OperationDetail operationDetail : operationDetails)
                operationDetailRepository.delete(operationDetail.getIdentifiant());
            tresorerieRepository.delete(tresorerie.getIdentifiant());
            miseAJourDeLaVersion(Version.MOTIF_SUPPRESSION,
                    tresorerie.getIdentifiant(), Tresorerie.class.getSimpleName());
        }
        Compte o = repository.findOne(compteId);
        if (super.delete(compteId)) {
            miseAJourDeLaVersion(o, Version.MOTIF_SUPPRESSION, compteId);
            return true;
        }
        return false;
    }

    @Override
    public RealisationParPrevisionList realisationParPrevision(Integer budgetId) {
        List<RealisationParPrevision> realisationParPrevisions = ((CompteRepository) repository)
                .findByCompteTypeNot(CompteType.TRESORERIE)
                .stream()
                .map(compte -> {
                    double sumPrevision = operationDetailRepository
                            .findByOperationBudgetIdentifiantAndCompteIdentifiant(
                                    budgetId, compte.getIdentifiant())
                            .stream().mapToDouble(OperationDetail::getMontant).sum();

                    double sumRealisation = operationDetailRepository
                            .findByOperationOperationBudgetBudgetIdentifiantAndCompteIdentifiant(
                                    budgetId, compte.getIdentifiant())
                            .stream().mapToDouble(OperationDetail::getMontant).sum();

                    RealisationParPrevision realisationParPrevision = new RealisationParPrevision();
                    realisationParPrevision.compte = compte;
                    realisationParPrevision.previsison = sumPrevision;
                    realisationParPrevision.realisation = sumRealisation;
                    realisationParPrevision.reste = sumPrevision - sumRealisation;
                    return realisationParPrevision;
                }).collect(Collectors.toList());

        RealisationParPrevisionList realisationParPrevisionList = new RealisationParPrevisionList();
        realisationParPrevisionList.realisationParPrevisions = realisationParPrevisions;
        realisationParPrevisionList.totalPrevision = realisationParPrevisions.stream()
                .mapToDouble(r -> r.previsison).sum();
        realisationParPrevisionList.totalRealisation = realisationParPrevisions.stream()
                .mapToDouble(r -> r.realisation).sum();
        realisationParPrevisionList.totalReste = realisationParPrevisionList.totalPrevision
                - realisationParPrevisionList.totalRealisation;

        return realisationParPrevisionList;
    }

    @Override
    public GrandLivreList recupererLeGrandLivre(Integer compteId, Date dateDebut, Date dateFin) {
        GrandLivreList grandLivreList = new GrandLivreList();
        grandLivreList.operationDetails = operationDetailRepository
                .findByOperationBudgetIsNullAndCompteIdentifiant(compteId);
        if (dateDebut != null) {
            grandLivreList.operationDetails = grandLivreList.operationDetails
                    .stream().filter(od -> od.getOperation().getDateEnregistement() != null
                            && (dateDebut.before(od.getOperation().getDateEnregistement()) ||
                            dateDebut.equals(od.getOperation().getDateEnregistement())))
                    .collect(Collectors.toList());
        }
        if (dateFin != null) {
            grandLivreList.operationDetails = grandLivreList.operationDetails
                    .stream().filter(od -> od.getOperation().getDateEnregistement() != null
                            && (dateFin.after(od.getOperation().getDateEnregistement()) ||
                            dateFin.equals(od.getOperation().getDateEnregistement())))
                    .collect(Collectors.toList());
        }
        grandLivreList.soldeTotal = grandLivreList.operationDetails.stream()
                .mapToDouble(OperationDetail::getMontant).sum();
        return grandLivreList;
    }

    @Override
    public List<Compte> recupererLaListeVersionnee(Integer[] ints) {
        return ((CompteRepository) repository).recupererLaListeVersionnee(ints);
    }

    @Override
    public Page<Compte> findAll(Pageable p) {
        Page<Compte> comptePage = super.findAll(p);
        List<Compte> comptes = comptePage.getContent().stream()
                .peek(c -> c.setSoldeCourant(operationDetailRepository
                        .findByCompteIdentifiant(c.getIdentifiant())
                        .stream().mapToDouble(OperationDetail::getMontant).sum()))
                .collect(Collectors.toList());
        return new PageImpl<>(comptes, new PageRequest(comptePage.getNumber(),
                comptePage.getSize()), comptePage.getTotalElements());
    }

    @Autowired
    public void setOperationDetailRepository(OperationDetailRepository operationDetailRepository) {
        this.operationDetailRepository = operationDetailRepository;
    }

    @Autowired
    public void setVersionRepository(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    @Autowired
    public void setTresorerieRepository(TresorerieRepository tresorerieRepository) {
        this.tresorerieRepository = tresorerieRepository;
    }
}
