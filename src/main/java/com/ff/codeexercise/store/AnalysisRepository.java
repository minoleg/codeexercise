package com.ff.codeexercise.store;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AnalysisRepository extends CrudRepository<Analysis, Integer> {

    List<Analysis> findAllByOrderByIdAsc();

}
