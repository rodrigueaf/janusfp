package com.gt.gestfinance.service.impl;

import com.gt.base.service.impl.BaseEntityService;
import com.gt.gestfinance.entity.OperationDetail;
import com.gt.gestfinance.entity.OperationSens;
import com.gt.gestfinance.entity.Tresorerie;
import com.gt.gestfinance.repository.OperationDetailRepository;
import com.gt.gestfinance.repository.TresorerieRepository;
import com.gt.gestfinance.service.ITresorerieService;
import com.gt.gestfinance.util.GrandLivreTresorerieList;
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

    @Autowired
    public TresorerieService(TresorerieRepository repository) {
        super(repository);
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

    @Autowired
    public void setOperationDetailRepository(OperationDetailRepository operationDetailRepository) {
        this.operationDetailRepository = operationDetailRepository;
    }

}
