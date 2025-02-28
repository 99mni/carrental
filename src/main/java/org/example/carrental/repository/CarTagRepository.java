package org.example.carrental.repository;

import org.example.carrental.DTO.CarTagResponseDTO;
import org.example.carrental.entity.CarTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CarTagRepository extends JpaRepository<CarTag, Long> {
    List<CarTag> findByTag_TagId(Long tagId);

    @Query("SELECT new org.example.carrental.DTO.CarTagResponseDTO(ct.car.id, ct.car.name, ct.car.brand, ct.car.status, ct.tag.tagId, ct.tag.name) " +
            "FROM CarTag ct WHERE ct.car.id = :carId")
    List<CarTagResponseDTO> findTagsByCarId(@Param("carId") Long carId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CarTag ct WHERE ct.tag.tagId = :tagId")
    void deleteByTagId(Long tagId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CarTag ct WHERE ct.car.id = :carId")
    void deleteByCarId(Long carId);



}