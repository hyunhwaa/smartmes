package mes.smartmes.service;

import mes.smartmes.entity.ProductionPlan;
import mes.smartmes.repository.ProdPlanRepository;
import mes.smartmes.repository.ProductionPlanRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;

@Service
public class ChangePlanService {
    private ProductionPlanRepository productionPlanRepository;

    public ChangePlanService(ProductionPlanRepository productionPlanRepository) {
        this.productionPlanRepository = productionPlanRepository;
    }

    public void processOrder(String planNo){
        // 작업지시완료 데이터를 가진 로우를 조회
        System.out.println("안녕하세요 여기");
        ProductionPlan pp = productionPlanRepository.findByPlanNo(planNo);
        ProductionPlan completedPlan = productionPlanRepository.findByProdPlanFinYn1("생산완료(출하완료)");
        System.out.println("안녕하세요~~~ = "+completedPlan);
        if (completedPlan != null) {
            // 다음 로우를 조회하여 대기중에서 진행중으로 변경
            ProductionPlan nextPlan = productionPlanRepository.findNextPlan(completedPlan.getProdPlanNo());
            System.out.println("안녕하세용ㅇㅇㅇ = "+nextPlan);
            if (nextPlan != null) {
                if(nextPlan.getProdPlanFinYn().equals("대기중") || nextPlan.getProdPlanFinYn().equals("발주입고완료") ){
                    nextPlan.setProdPlanFinYn("진행중");
                    //pp.setProdPlanFinYn("생산완료(출하완료!)");
                }
                productionPlanRepository.save(pp);
                productionPlanRepository.flush();
                productionPlanRepository.save(nextPlan);

            }
        }
    }

    public void processOrder1(String planNo){
        List<ProductionPlan> plan = productionPlanRepository.findAll();
        ProductionPlan pp = productionPlanRepository.findByPlanNo(planNo);
        System.out.println("플랜바꾸기 - "+plan);
        System.out.println("플랜바꾸기 - "+plan.get(0).getProdPlanNo());
        System.out.println(planNo);
        if(plan.get(0).getProdPlanNo().equals(planNo)){
            pp.setProdPlanFinYn("진행중");
            productionPlanRepository.save(pp);
            productionPlanRepository.flush();
        }

    }

    public void processOrder12(String planNo){
        List<ProductionPlan> plan = productionPlanRepository.findAll();
        ProductionPlan pp = productionPlanRepository.findByPlanNo(planNo);
        System.out.println("플랜바꾸기 - "+plan);
        System.out.println("플랜바꾸기 - "+plan.get(0).getProdPlanNo());
        System.out.println(planNo);
        if(plan.get(0).getProdPlanNo().equals(planNo)){
            pp.setProdPlanFinYn("진행중");
            productionPlanRepository.save(pp);
            productionPlanRepository.flush();
        }

    }


    @Scheduled(cron = "*/15 * * * * ?") // 30초 마다 실행
    public void processOrdersAutomatically() {
        List<ProductionPlan> plans = productionPlanRepository.findByProdPlanFinYn("생산완료(출하완료)");
        if (plans != null && !plans.isEmpty()) {
            for (ProductionPlan plan : plans) {
                int planQuantity = plan.getProdPlanQuantity();
                String planNo = plan.getProdPlanNo();
                System.out.println("안녕하세요 여기~~~");
                processOrder(planNo);
            }
        }
        List<ProductionPlan> planss = productionPlanRepository.findByProdPlanFinYn("대기중");
        if (planss != null && !planss.isEmpty()) {
            for (ProductionPlan plan : planss) {
                int planQuantity = plan.getProdPlanQuantity();
                String planNo = plan.getProdPlanNo();
                System.out.println("안녕하세요 여기~~~");
                processOrder1(planNo);
            }
        }

        List<ProductionPlan> planss1 = productionPlanRepository.findByProdPlanFinYn("발주입고완료");
        if (planss != null && !planss.isEmpty()) {
            for (ProductionPlan plan : planss) {
                int planQuantity = plan.getProdPlanQuantity();
                String planNo = plan.getProdPlanNo();
                System.out.println("안녕하세요 여기~~~");
                processOrder12(planNo);
            }
        }

    }
}
