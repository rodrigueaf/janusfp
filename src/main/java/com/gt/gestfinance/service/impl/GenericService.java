package com.gt.gestfinance.service.impl;

import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.repository.GenericRepository;
import com.gt.gestfinance.service.IGenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 * Classe implémentant l'interface <code>IGenericService</code>
 *
 * @param <T> : L'entié
 * @param <I> : La clé primaire
 * @author <a href="mailto:ali.amadou@ace3i.com?">Yasser Ali</a>,
 * <a href="mailto:afandi-ekoue.amah-tchoutchoui@ace3i.com?">Ekoue</a>
 */
@Transactional(rollbackFor = {Throwable.class})
public abstract class GenericService<T, I extends Serializable> implements IGenericService<T, I> {

    /**
     * Une instance de repository pour tous les services dérivés de cette
     * classe.
     */
    protected GenericRepository<T, I> repository;

    /**
     * Une instance de logger pour tous les services dérivés de cette classe.
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Constructeur parent pour récupérer l'instance de chaque classe dérivé à
     * utiliser
     *
     * @param repository : une instance de repository
     */
    public GenericService(GenericRepository<T, I> repository) {
        super();
        this.repository = repository;
    }

    /**
     * @see com.gt.gestfinance.service.IGenericService#save(Object)
     */
    @Override
    public synchronized T save(T entity) throws CustomException {
        return this.repository.save(entity);
    }

    /**
     * @see com.gt.gestfinance.service.IGenericService#save(Iterable)
     */
    @Override
    public synchronized List<T> save(Iterable<T> entities) {
        return this.repository.save(entities);
    }

    /**
     * @see com.gt.gestfinance.service.IGenericService#saveAndFlush(Object)
     */
    @Override
    public synchronized T saveAndFlush(T entity) throws CustomException {
        return this.repository.saveAndFlush(entity);
    }

    /**
     * @see com.gt.gestfinance.service.IGenericService#saveAndFlush(List)
     */
    @Override
    public List<T> saveAndFlush(List<T> entities) {

        List<T> result = new LinkedList<>();
        entities.forEach(t -> result.add(this.repository.saveAndFlush(t)));
        return result;
    }

    /**
     * @see com.gt.gestfinance.service.IGenericService#findOne(Serializable)
     */
    @Override
    public T findOne(I id) {
        return this.repository.findOne(id);
    }

    /**
     * @see com.gt.gestfinance.service.IGenericService#delete(Serializable)
     */
    @Override
    public boolean delete(I id) {
        this.repository.delete(id);
        return true;
    }

    /**
     * @see com.gt.gestfinance.service.IGenericService#delete(Object)
     */
    @Override
    public boolean delete(T entity) {
        this.repository.delete(entity);
        return true;
    }

    /**
     * @see com.gt.gestfinance.service.IGenericService#deleteAll()
     */
    @Override
    public boolean deleteAll() {
        this.repository.deleteAll();
        return true;
    }

    /**
     * @see com.gt.gestfinance.service.IGenericService#findAll()
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return this.repository.findAll();
    }

    /**
     * @see com.gt.gestfinance.service.IGenericService#findAll(Pageable)
     */
    @Override
    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable p) {
        return this.repository.findAll(p);
    }

    @Override
    public Page<T> findAllByCriteres(Specification<T> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    /**
     * @see com.gt.gestfinance.service.IGenericService#count()
     */
    @Override
    public long count() {
        return this.repository.count();
    }

    /**
     * @see com.gt.gestfinance.service.IGenericService#isExist(Serializable)
     */
    @Override
    public boolean isExist(I i) {
        try {
            return this.findOne(i) != null;
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(GenericService.class.getName()).log(Level.INFO, null, e);
            return false;
        }
    }
}
