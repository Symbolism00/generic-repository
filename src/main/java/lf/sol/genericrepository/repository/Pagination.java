package lf.sol.genericrepository.repository;

import lf.sol.genericrepository.model.GenericEntity;
import lombok.Getter;

import java.util.List;

@Getter
public class Pagination<T extends GenericEntity> {

    private final List<T> results;
    private final Long count;

    protected Pagination(List<T> results, Long count) {
        this.results = results;
        this.count = count;
    }
}
