/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.gestfinance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Implémentation de base des repository
 *
 * @author <a href="mailto:ali.amadou@ace3i.com?">Yasser Ali</a>
 * 
 * @param <T> : La classe entité
 * @param <I> : La clé primaire
 */
@NoRepositoryBean
public interface GenericRepository<T, I extends Serializable> extends JpaRepository<T , I>, JpaSpecificationExecutor<T>{
}
