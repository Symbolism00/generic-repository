package lf.sol.abstractrepository.repository;

import lf.sol.abstractrepository.model.AbstractEntity;
import lombok.Getter;

import java.util.List;

@Getter
public class Pagination<T extends AbstractEntity> {

    private final List<T> results;
    private final Long count;

    protected Pagination(List<T> results, Long count) {
        this.results = results;
        this.count = count;
    }
}
