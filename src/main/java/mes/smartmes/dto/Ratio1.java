package mes.smartmes.dto;

import mes.smartmes.entity.ProductionPlan;

public class Ratio1 {
    private int quantity;

    public Ratio1(int quantity) {
        this.quantity = quantity;
    }

    public double getOrderInput() {
        return (quantity*30)/20;
    }

    //양배추 생산량
    public double cabbageQty(){
        System.out.println("라티오1 : "+getOrderInput());
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
        return getOrderInput();
    }

    //양배추+물(kg)
    public double getCabbageWater() {
        return getOrderInput() + getCabbageWaterInput();
    }

    //양배추즙 추출액(kg)
    public double getCabbageWaterOutput() {
        return getCabbageWater() * 0.8;
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
