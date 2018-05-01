package com.gt.gestfinance.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Path;
import java.util.Date;

/**
 * Spécification de base pour les requetes basé sur critéria
 *
 * @author <a href="mailto:ali.amadou@ace3i.com?">Yasser Ali</a>
 * @version 1
 *
 */
public class BaseSpecifications {

    /**
     * Le contructeur
     */
    public BaseSpecifications() {
        // do something
    }

    /**
     * Vérifie si la valeur est contenu
     *
     * @param attribute
     * @param value
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Specification containsLike(String attribute, String value) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(attribute).as(String.class)), "%" + value.toLowerCase() + "%");
    }

    /**
     * Vérifie si la valeur est au début
     *
     * @param attribute
     * @param value
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Specification startWith(String attribute, String value) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(attribute).as(String.class)), value.toLowerCase() + "%");
    }

    /**
     * Vérifie si la valeur est à la fin
     *
     * @param attribute
     * @param value
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Specification endWith(String attribute, String value) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(attribute).as(String.class)), "%" + value.toLowerCase());
    }

    /**
     * Vérifie si la valeur est égale
     *
     * @param attribute
     * @param value
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Specification isEqual(String attribute, Date value) {
        return (root, query, cb) -> cb.equal(root.get(attribute).as(Date.class), value);
    }

    /**
     * Vérifie si la valeur est entre les paramètres passés
     *
     * @param attribute
     * @param min
     * @param max
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Specification isBetween(String attribute, int min, int max) {
        return (root, query, cb) -> cb.between(root.get(attribute), min, max);
    }

    /**
     * Vérifie si la valeur est entre les paramètres passés
     *
     * @param attribute
     * @param min
     * @param max
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Specification isBetween(String attribute, double min, double max) {
        return (root, query, cb) -> cb.between(root.get(attribute), min, max);
    }

    /**
     * Vérifie si la valeur est égale
     *
     * @param <T>
     * @param attribute
     * @param queriedValue
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Enum<T>> Specification enumMatcher(String attribute, T queriedValue) {
        return (root, query, cb) -> {
            Path<T> actualValue = root.get(attribute);

            if (queriedValue == null) {
                return null;
            }

            return cb.equal(actualValue, queriedValue);
        };
    }

    /**
     * Renvoie une chaine de caratère
     *
     * @return
     */
    @Override
    public String toString() {
        return "BaseSpecifications : []";
    }
}
