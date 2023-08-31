package mes.smartmes.dto;

import mes.smartmes.entity.ProductionPlan;
import mes.smartmes.repository.ProductionPlanRepository;
import mes.smartmes.service.WorkOrderService;

public class Ratio {
    private ProductionPlan productionPlan;

    public Ratio(ProductionPlan productionPlan) {
        this.productionPlan = productionPlan;
    }

    //productionPlan.getProdPlanQuantity : 생산계획에서 준 생산 투입량
    // (양배추즙, 흑마늘즙: 양배추, 흑마늘 투입량 (단위:kg) / 젤리: 농축액의 양 (단위:L)

    public double getOrderInput() {
        return productionPlan.getProdPlanQuantity();
    }

    //양배추 생산량
    public double cabbageQty(){
        return getOrderInput()* 20;
    }
    //흑마늘즙 생산량
    public double blackGarlicQty(){
        return  getOrderInput()*120;
    }
    //젤리 생산량
    public double jellyQty(){
        return getJellyInputQty()*1000/80;
    }

    public double getWaterOrderInputQty() {
        return getOrderInput() * 30;
    }

    public double getJellyOrderInputQty() {
        return getOrderInput() * 25;
    }

    //양배추 투입량(kg)
    public double getCabbageInputQty() {
        return getWaterOrderInputQty() / 20;
    }

    //양배추즙 생산 시 물 투입량(kg)
    public double getCabbageWaterInput() {
        return productionPlan.getProdPlanQuantity();
    }

    //양배추+물(kg)
    public double getCabbageWater() {
        return getOrderInput() + getCabbageWaterInput();
    }

    //양배추즙 추출액(kg)
    public double getCabbageWaterOutput() {
        return getCabbageWater() * 0.8;
    }

    public double getCabbageWaterOutput1() {
        return getCabbageWaterOutput()*1000.0/80;
    }

    //흑마늘 투입량(kg)
    public double getGarlicInputQty() {
        return getOrderInput();
    }

    //흑마늘즙 생산 시 물 투입량(kg)
    public double getGarlicWaterInput() {
        return (getOrderInput() * 3);
    }

    //흑마늘+물(kg)
    public double getBlackGarlicWater() {
        return getOrderInput() + getGarlicWaterInput();
    }

    //흑마늘즙 생산량(kg)
    public double getBlackGarlicOutput() {
        return getBlackGarlicWater() * 0.6;
    }

    //젤리 생산 시 투입량(L)
    public double getJellyInputQty() {
        return (getOrderInput()/5) * 15;                    //250
    }
}









