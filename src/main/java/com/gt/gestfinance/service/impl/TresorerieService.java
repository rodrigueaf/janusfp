package com.gt.gestfinance.service.impl;

import com.gt.gestfinance.entity.OperationDetail;
import com.gt.gestfinance.entity.OperationSens;
import com.gt.gestfinance.entity.Tresorerie;
import com.gt.gestfinance.entity.Version;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.repository.CompteRepository;
import com.gt.gestfinance.repository.OperationDetailRepository;
import com.gt.gestfinance.repository.TresorerieRepository;
import com.gt.gestfinance.repository.VersionRepository;
import com.gt.gestfinance.service.ITresorerieService;
import com.gt.gestfinance.util.GrandLivreTresorerieList;
import com.gt.gestfinance.util.MPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe Service de l'entité Tresorerie
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 31/03/2018
 */
@Service
public class TresorerieService extends BaseEntityService<Tresorerie, Integer> implements ITresorerieService {

    private OperationDetailRepository operationDetailRepository;
    private VersionRepository versionRepository;
    private CompteRepository compteRepository;

    @Autowired
    public TresorerieService(TresorerieRepository repository) {
        super(repository);
    }

    @Override
    public VersionRepository getVersionRepository() {
        return versionRepository;
    }

    @Override
    public synchronized Tresorerie save(Tresorerie entity) throws CustomException {
        reconstructionDeLaTresorerie(entity);
        Tresorerie tresorerie = super.save(entity);
        miseAJourDeLaVersion(tresorerie, Version.MOTIF_AJOUT, tresorerie.getIdentifiant());
        return tresorerie;
    }

    private void reconstructionDeLaTresorerie(Tresorerie tresorerie) {
        if (tresorerie.getCompteLogique() != null && tresorerie.getCompteLogique().getIdentifiant() != null) {
            tresorerie.setCompteLogique(compteRepository.findOne(tresorerie.getCompteLogique().getIdentifiant()));
        }
        if (tresorerie.getComptePhysique() != null && tresorerie.getComptePhysique().getIdentifiant() != null) {
            tresorerie.setComptePhysique(compteRepository.findOne(tresorerie.getComptePhysique().getIdentifiant()));
        }
    }

    @Override
    public synchronized Tresorerie saveAndFlush(Tresorerie entity) throws CustomException {
        reconstructionDeLaTresorerie(entity);
        Tresorerie tresorerie = super.saveAndFlush(entity);
        miseAJourDeLaVersion(tresorerie, Version.MOTIF_MODIFICATION, tresorerie.getIdentifiant());
        return tresorerie;
    }

    @Override
    public boolean supprimer(Integer id) throws CustomException {
        if (!operationDetailRepository.findByTresorerieIdentifiant(id).isEmpty()) {
            throw new CustomException(MPConstants.LA_TRESORERIE_DEJA_UTILISER);
        }
        Tresorerie tresorerie = findOne(id);
        if (super.delete(id)) {
            miseAJourDeLaVersion(tresorerie, Version.MOTIF_SUPPRESSION, id);
            return true;
        }
        return false;
    }

    @Override
    public boolean supprimerForcer(Integer tresorerieId) {
        return false;
    }

    @Override
    public Page<Tresorerie> findAll(Pageable p) {
        Page<Tresorerie> tresoreriePage = super.findAll(p);
        List<Tresorerie> tresoreries = tresoreriePage.getContent().stream()
                .peek(c -> c.setSoldeCourant(calculerLeSolde(c)))
                .collect(Collectors.toList());
        return new PageImpl<>(tresoreries, new PageRequest(tresoreriePage.getNumber(),
                tresoreriePage.getSize()), tresoreriePage.getTotalElements());
    }

    @Override
    public Double calculerLeSolde(Tresorerie tresorerie) {
        double sommeCredit = operationDetailRepository
                .findByOperationBudgetIsNullAndTresorerieIdentifiantAndOperationSens(tresorerie
                        .getIdentifiant(), OperationSens.CREDIT)
                .stream().mapToDouble(OperationDetail::getMontant).sum();
        double sommeDebit = operationDetailRepository
                .findByOperationBudgetIsNullAndTresorerieIdentifiantAndOperationSens(tresorerie
                        .getIdentifiant(), OperationSens.DEBIT)
                .stream().mapToDouble(OperationDetail::getMontant).sum();
        return sommeDebit - sommeCredit;
    }

    @Override
    public GrandLivreTresorerieList recupererLeGrandLivre(Integer tresorerieId, Date dateDebut, Date dateFin) {
        GrandLivreTresorerieList grandLivreTresorerieList = new GrandLivreTresorerieList();
        grandLivreTresorerieList.operationDetails = operationDetailRepository
                .findByOperationBudgetIsNullAndTresorerieIdentifiant(tresorerieId);
        if (dateDebut != null) {
            grandLivreTresorerieList.operationDetails = grandLivreTresorerieList.operationDetails
                    .stream().filter(od -> od.getOperation().getDateEnregistement() != null
                            && (dateDebut.before(od.getOperation().getDateEnregistement()) ||
                            dateDebut.equals(od.getOperation().getDateEnregistement())))
                    .collect(Collectors.toList());
        }
        if (dateFin != null) {
            grandLivreTresorerieList.operationDetails = grandLivreTresorerieList.operationDetails
                    .stream().filter(od -> od.getOperation().getDateEnregistement() != null
                            && (dateFin.after(od.getOperation().getDateEnregistement()) ||
                            dateFin.equals(od.getOperation().getDateEnregistement())))
                    .collect(Collectors.toList());
        }
        grandLivreTresorerieList.soldeCreditTotal = grandLivreTresorerieList.operationDetails.stream()
                .filter(o -> o.getOperationSens() == OperationSens.CREDIT)
                .mapToDouble(OperationDetail::getMontant).sum();
        grandLivreTresorerieList.soldeDebitTotal = grandLivreTresorerieList.operationDetails.stream()
                .filter(o -> o.getOperationSens() == OperationSens.DEBIT)
                .mapToDouble(OperationDetail::getMontant).sum();
        return grandLivreTresorerieList;
    }

    @Override
    public List<Tresorerie> recupererLaListeVersionnee(Integer[] ints) {
        return ((TresorerieRepository) repository).recupererLaListeVersionnee(ints);
    }

    @Autowired
    public void setVersionRepository(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    @Autowired
    public void setOperationDetailRepository(OperationDetailRepository operationDetailRepository) {
        this.operationDetailRepository = operationDetailRepository;
    }

    @Autowired
    public void setCompteRepository(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }
}
