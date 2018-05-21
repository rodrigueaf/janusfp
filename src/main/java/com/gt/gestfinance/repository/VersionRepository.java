package com.gt.gestfinance.repository;

import com.gt.gestfinance.entity.Version;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VersionRepository extends BaseEntityRepository<Version, Integer> {

    @Query("select c from Version c where c.attribut = :attribut and c.valeur > :valeur order by valeur desc")
    public List<Version> selectionnerListe(@Param("attribut") String attribut,
                                           @Param("valeur") Integer valeur);

    @Query("select c from Version c where c.attribut = :attribut order by valeur desc")
    public List<Version> listerValeurDesc(@Param("attribut") String attribut);
}
