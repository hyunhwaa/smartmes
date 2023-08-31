package mes.smartmes.service;

import lombok.RequiredArgsConstructor;
import mes.smartmes.entity.ProdPerform;
import mes.smartmes.repository.ProdPerformRepository;
import mes.smartmes.repository.ProdPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProdPerformService {
    private final ProdPerformRepository prodPerformRepository;

    public List<ProdPerform> selectList() {
        return prodPerformRepository.findAll();
    }

}
