package mes.smartmes.service;

import mes.smartmes.dto.Ratio;
import mes.smartmes.dto.Ratio1;
import mes.smartmes.dto.Weekday;
import mes.smartmes.entity.ProductionPlan;
import mes.smartmes.entity.Routing;
import mes.smartmes.repository.OrdersRepository;
import mes.smartmes.repository.RoutingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CheckQuantityService {

    private Ratio1 ratio;
    private Ratio ratio1;
    private final OrdersRepository ordersRepository;
    private final RoutingRepository routingRepository;
    private ProductionPlan productionPlan;



    public CheckQuantityService(OrdersRepository ordersRepository, RoutingRepository routingRepository) {
        this.ordersRepository = ordersRepository;
        this.routingRepository = routingRepository;
    }


    public Map<String, Object> countTime1(String productId, LocalDateTime date, int quantity) {
        System.out.println("카운트타임1 프로덕트아이디 - " + productId);
        ArrayList<String> processList = selectProcess(productId);
        double workLeadTime = 0; // 공정별 총 리드타임
        double workProcessTime = 0; // 공정별 총 소요시간

        LocalDateTime totalProcessTime = date;  //총시간 = 끝난시간
        if(totalProcessTime == null){
            totalProcessTime = LocalDateTime.now();
        }
        LocalDateTime currentTime = LocalDateTime.now();  //시작시간
        System.out.println("카운트타임1 토탈프로세스타임 - "+totalProcessTime);
        System.out.println("카운트타임1 퀀티티 - "+quantity);
        //ratio = new Ratio(productionPlan);
        ratio = new Ratio1(quantity);

        Map<String, LocalDateTime> processTimes = new HashMap<>();
        Map<String, LocalDateTime> processStartTimes = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        processTimes.put("process0",totalProcessTime);


        for (int i = 0; i < processList.size(); i++) {
            String process = processList.get(i);
            System.out.println("프로세스"+(i+1)+"번 들어옴"+ totalProcessTime);
            long leadTime = ordersRepository.findLeadTime(process);
            long processTime = ordersRepository.findProcessTime(process);
            long capa = ordersRepository.findCapa(process);
            long cnt = 0;

            switch (process) {
                case "process01":
                    process01(processTime, workProcessTime, cnt);
                    System.out.println("in-process01");
                    AbstractMap.SimpleEntry<Double, Long> result = process01(processTime, workProcessTime, cnt);
                    workProcessTime = result.getKey();//30.0
//                    System.out.println("시발시발시발 - "+String.valueOf(totalProcessTime));
//                    System.out.println("시발시발시발 - "+ totalProcessTime + workProcessTime);
                    cnt = result.getValue(); //1
                    System.out.println("프로세스1번토탈 - "+totalProcessTime);
                    System.out.println("프로세스1번토탈 - "+workProcessTime);
                    System.out.println("프로세스1번토탈 - "+cnt);
                    //processTimes.put(process, totalProcessTime);
                    System.out.println(processTimes.get("process01"));

                    break;
                case "process02":
                    process02(processTime, productId, workProcessTime, cnt);
                    System.out.println("in-process02");
                    AbstractMap.SimpleEntry<Double, Long> result1 = process02(processTime, productId, workProcessTime, cnt);
                    workProcessTime = result1.getKey();
                    cnt = result1.getValue();
                    System.out.println("프로세스2 : "+workProcessTime);
                    System.out.println("프로세스2 : "+cnt);

                    //processTimes.put(process, totalProcessTime);
                    break;
                case "process03":
                    process03(processTime, capa, productId, (long) workProcessTime, cnt);
                    System.out.println("in-process03");
                    AbstractMap.SimpleEntry<Double, Long> result2 = process03(processTime,capa, productId, (long) workProcessTime, cnt);
                    workProcessTime = result2.getKey();
                    cnt = result2.getValue();
                    System.out.println("프로세스3번토탈 - "+totalProcessTime);
                    System.out.println("프로세스3번토탈 - "+workProcessTime);
                    System.out.println("프로세스3번토탈 - "+cnt);
                    //processTimes.put(process, totalProcessTime);
                    break;
                case "process04":
                    process04(processTime, productId, (long) workProcessTime, cnt);
                    System.out.println("in-process04");
                    AbstractMap.SimpleEntry<Double, Long> result3 = process04(processTime, productId, (long) workProcessTime, cnt);
                    workProcessTime = result3.getKey();
                    cnt = result3.getValue();
                    System.out.println("프로세스4번토탈 - "+totalProcessTime);
                    System.out.println("프로세스4번토탈 - "+workProcessTime);
                    System.out.println("프로세스4번토탈 - "+cnt);
                    //processTimes.put(process, totalProcessTime);
                    break;
                case "process05":
                    process05(processTime, (long) workProcessTime, cnt);
                    System.out.println("in-process05");
                    AbstractMap.SimpleEntry<Double, Long> result4 = process05(processTime, (long) workProcessTime, cnt);
                    workProcessTime = result4.getKey();
                    cnt = result4.getValue();
                    System.out.println("프로세스5번토탈 - "+totalProcessTime);
                    System.out.println("프로세스5번토탈 - "+workProcessTime);
                    System.out.println("프로세스5번토탈 - "+cnt);
                    //processTimes.put(process, totalProcessTime);
                    break;
                case "process06":
                    process06(totalProcessTime);
                    System.out.println("in-process06");
                    totalProcessTime = process06(totalProcessTime);
                    workProcessTime = 1440.0;
                    System.out.println("프로세스6번토탈 - "+totalProcessTime);
                    System.out.println("프로세스6번토탈 - "+workProcessTime);
                    System.out.println("프로세스6번토탈 - "+cnt);
                    //processTimes.put(process, process06work);
                    break;
                case "process07":
                    process07(processTime, capa, (long) workProcessTime, cnt);
                    System.out.println("in-process07");
                    AbstractMap.SimpleEntry<Double, Long> result5 = process07(processTime, capa, (long) workProcessTime, cnt);
                    workProcessTime = result5.getKey();
                    cnt = result5.getValue();
                    System.out.println("프로세스7번토탈 - "+totalProcessTime);
                    System.out.println("프로세스7번토탈 - "+workProcessTime);
                    System.out.println("프로세스7번토탈 - "+cnt);
                    //processTimes.put(process, totalProcessTime);
                    break;
                case "process08":
                    process08(processTime, (long) workProcessTime, cnt);
                    System.out.println("in-process08");
                    AbstractMap.SimpleEntry<Double, Long> result6 = process08(processTime, (long) workProcessTime, cnt);
                    workProcessTime = result6.getKey();
                    cnt = result6.getValue();
                    System.out.println("프로세스8번토탈 - "+totalProcessTime);
                    System.out.println("프로세스8번토탈 - "+workProcessTime);
                    System.out.println("프로세스8번토탈 - "+cnt);
                    //processTimes.put(process, totalProcessTime);
                    break;
                case "process09":
                    process09(processTime, capa, productId, (long) workProcessTime, cnt);
                    System.out.println("in-process09");
                    AbstractMap.SimpleEntry<Double, Long> result7 = process09(processTime,capa,productId, (long) workProcessTime, cnt);
                    workProcessTime = result7.getKey();
                    cnt = result7.getValue();
                    System.out.println("프로세스9번토탈 - "+totalProcessTime);
                    System.out.println("프로세스9번토탈 - "+workProcessTime);
                    System.out.println("프로세스9번토탈 - "+cnt);
                    //processTimes.put(process, totalProcessTime);
                    break;
                case "process10":
                    process10(processTime, capa, (long) workProcessTime, cnt);
                    System.out.println("in-process10");
                    AbstractMap.SimpleEntry<Double, Long> result8 = process10(processTime,capa, (long) workProcessTime, cnt);
                    workProcessTime = result8.getKey();
                    cnt = result8.getValue();
                    System.out.println("프로세스10번토탈 - "+totalProcessTime);
                    System.out.println("프로세스10번토탈 - "+workProcessTime);
                    System.out.println("프로세스10번토탈 - "+cnt);
                    //processTimes.put(process, totalProcessTime);
                    break;
            }
            // 작업 시간 정보를 리스트에 저장
            List<String> list = new ArrayList<>();
            //totalProcessTime = processTimes.get(process);
            //LocalDateTime totalProcessTime = productionPlan.getProdPlanDate();
            System.out.println("여기여기여기1111 - "+totalProcessTime);

            if (cnt == 0) {

                list.add(String.valueOf(totalProcessTime));
                list.add(process);
                processTimes.put(process, totalProcessTime);
                processStartTimes.put(process, currentTime);
                System.out.println("프로세스타임(cnt 0) - "+totalProcessTime);
            } else {
                // 수작업 //30.0, 1
                if (process.equals("process01") || process.equals("process10")) {
                    for (int j = 0; j < cnt; j++) {
                        if (j == cnt - 1) {
                            long time = (long) workProcessTime - (processTime * (j)); //30
                            System.out.println("프로세스1들어옴 - "+totalProcessTime);
                            currentTime = selfTimeCheck(totalProcessTime, leadTime, time);
                            totalProcessTime = currentTime.plusMinutes(leadTime+time);
                            System.out.println("프로세스1번이프문 - "+totalProcessTime);
                        } else {
                            currentTime = selfTimeCheck(totalProcessTime, leadTime, processTime);
                            totalProcessTime = selfTimeCheck(totalProcessTime, leadTime, processTime);
                        }
                        list.add(String.valueOf(totalProcessTime));

                        if (!list.isEmpty() && list.get(j).length() != 0) {
                            if (j >= 1) {
                                System.out.println("11111111111111111111");
                                System.out.println(totalProcessTime);
                                //totalProcessTime = currentTime;
                                System.out.println("2222222222222222222222");
                                System.out.println(totalProcessTime);
                            }
                        }
                    }
                    // 자동작업
                } else {

                    for (int j = 0; j < cnt; j++) {
                        if (j == cnt - 1) {
                            long time = (long) workProcessTime - (processTime * (j));
                            System.out.println("여기시발 - "+String.valueOf(totalProcessTime));
                            if(process.equals("process04")){
                                currentTime = processTimes.get("process03");
                            }else{
                                currentTime = autoTimeCheck(totalProcessTime, leadTime, time);
                            }
                            totalProcessTime = currentTime.plusMinutes(leadTime+time);
                        } else {
                            System.out.println("여기시발1 - "+totalProcessTime);
                            if(process.equals("process04")){
                                currentTime = processTimes.get("process03");
                            }else{
                                currentTime = autoTimeCheck(totalProcessTime, leadTime, processTime);
                            }
                            totalProcessTime = currentTime.plusMinutes(leadTime+processTime);
                        }
                        list.add(String.valueOf(totalProcessTime));

                        if (!list.isEmpty() && list.get(j).length() != 0) {
                            if (j >= 1) {
                                System.out.println("11111111111111111111");
                                System.out.println(totalProcessTime);
                                //totalProcessTime = currentTime;
                                System.out.println("2222222222222222222222");
                                System.out.println(totalProcessTime);
                            }
                        }
                    }
                }
                list.add(process);
                processTimes.put(process, totalProcessTime);
                processStartTimes.put(process, currentTime);
                System.out.println("프로세스타임 - "+processTimes);
                System.out.println("프로세스스타트타임 - "+processStartTimes);

                resultMap.put("processTimes", processTimes);
                resultMap.put("processStartTimes", processStartTimes);




            }
            System.out.println("==========================");
            System.out.println("list = " + list);
            System.out.println("==========================");
            System.out.println("processNo = " + process);
            System.out.println("cnt = " + cnt);
            System.out.println("processTime = " + processTime);
            System.out.println("==========================");



        }

        //System.out.println("Here!! - "+ totalProcessTime);
        System.out.println("process01 - "+processTimes.get("process01"));
        System.out.println("process02 - "+processTimes.get("process02"));
        System.out.println("process03 - "+processTimes.get("process03"));
        System.out.println("process04 - "+processTimes.get("process04"));
        System.out.println("process05 - "+processTimes.get("process05"));
        System.out.println("process06 - "+processTimes.get("process06"));
        System.out.println("process07 - "+processTimes.get("process07"));
        System.out.println("process08 - "+processTimes.get("process08"));
        System.out.println("process09 - "+processTimes.get("process09"));
        System.out.println("process10 - "+processTimes.get("process10"));

        System.out.println("process01 - "+processStartTimes.get("process01"));
        System.out.println("process02 - "+processStartTimes.get("process02"));
        System.out.println("process03 - "+processStartTimes.get("process03"));
        System.out.println("process04 - "+processStartTimes.get("process04"));
        System.out.println("process05 - "+processStartTimes.get("process05"));
        System.out.println("process06 - "+processStartTimes.get("process06"));
        System.out.println("process07 - "+processStartTimes.get("process07"));
        System.out.println("process08 - "+processStartTimes.get("process08"));
        System.out.println("process09 - "+processStartTimes.get("process09"));
        System.out.println("process10 - "+processStartTimes.get("process10"));
        return resultMap;
    }

    public AbstractMap.SimpleEntry<Double, Long> process01(long processTime, double workProcessTime, long cnt) {
        System.out.println("inM-process01");
        System.out.println("시발시발 - "+processTime);
        System.out.println("시발시발 - "+workProcessTime);
        workProcessTime = processTime;
        System.out.println("시발시발 - "+workProcessTime);
        cnt = 1;
        // 추가 작업
        return new AbstractMap.SimpleEntry<>(workProcessTime, cnt);
    }

    private AbstractMap.SimpleEntry<Double, Long> process02(long processTime, String productId, double workProcessTime, long cnt) {
        System.out.println("inM-process02");

        if (productId.equals("P001")) {
            workProcessTime = ratio.getCabbageInputQty() / 1000 * processTime;
            cnt = (long) Math.ceil(ratio.getCabbageInputQty() / 1000);
        } else if (productId.equals("P002")) {
            workProcessTime = ratio.getGarlicInputQty() / 1000 * processTime;
            cnt = (long) Math.ceil(ratio.getGarlicInputQty() / 1000);
        }
        // 추가 작업
        return new AbstractMap.SimpleEntry<>(workProcessTime, cnt);
    }

    private AbstractMap.SimpleEntry<Double, Long> process03(long processTime, long capa, String productId, long cnt, double workProcessTime) {
        System.out.println("inM-process03");

        if (productId.equals("P001")) {
            workProcessTime = ratio.getCabbageWater() / capa * processTime;
            cnt = (long) Math.ceil(ratio.getCabbageWater() / 2000);
        } else if (productId.equals("P002")) {
            workProcessTime = ratio.getBlackGarlicWater() / capa * processTime;
            cnt = (long) Math.ceil(ratio.getBlackGarlicWater() / 2000);
        }
        // 추가 작업
        return new AbstractMap.SimpleEntry<>(workProcessTime, cnt);
    }

    private AbstractMap.SimpleEntry<Double, Long> process04(long processTime, String productId, long cnt, double workProcessTime) {
        System.out.println("inM-process04");

        if (productId.equals("P001")) {
            workProcessTime = Math.ceil(ratio.getCabbageWaterOutput() / 2000) * processTime;
            cnt = (long) Math.ceil(ratio.getCabbageWaterOutput() / 2000);
        } else if (productId.equals("P002")) {
            workProcessTime = Math.ceil(ratio.getBlackGarlicOutput() / 2000) * processTime;
            cnt = (long) Math.ceil(ratio.getBlackGarlicOutput() / 2000);
        }
        // 추가 작업
        return new AbstractMap.SimpleEntry<>(workProcessTime, cnt);
    }

    private AbstractMap.SimpleEntry<Double, Long> process05(long processTime, long cnt, double workProcessTime) {
        System.out.println("inM-process05");

        workProcessTime = Math.ceil(ratio.getJellyInputQty() / 2000000) * processTime;
        cnt = (long) Math.ceil(ratio.getJellyInputQty() / 2000000);
        // 추가 작업
        return new AbstractMap.SimpleEntry<>(workProcessTime, cnt);
    }

    private LocalDateTime process06(LocalDateTime totalProcessTime) {
        System.out.println("inM-process06");

        totalProcessTime = setTime(totalProcessTime, 1, 9);
        // 추가 작업
        return totalProcessTime;
    }

    private AbstractMap.SimpleEntry<Double, Long> process07(long processTime, long capa, long cnt, double workProcessTime) {
        System.out.println("inM-process07");

        workProcessTime = (double) (ratio.getWaterOrderInputQty() / 3500) * processTime;
        cnt = (long) Math.ceil(ratio.getWaterOrderInputQty() / 3500);
        // 추가 작업
        return new AbstractMap.SimpleEntry<>(workProcessTime, cnt);
    }

    private AbstractMap.SimpleEntry<Double, Long> process08(long processTime, long cnt, double workProcessTime) {
        System.out.println("inM-process08");

        workProcessTime = (double) ratio.getJellyOrderInputQty() / 3000 * processTime;
        cnt = (long) Math.ceil(ratio.getJellyOrderInputQty() / 3000);
        // 추가 작업
        return new AbstractMap.SimpleEntry<>(workProcessTime, cnt);
    }

    private AbstractMap.SimpleEntry<Double, Long> process09(long processTime, long capa, String productId, long cnt, double workProcessTime) {
        System.out.println("inM-process09");

        if (productId.equals("P001") || productId.equals("P002")) {
            workProcessTime = (ratio.getWaterOrderInputQty() / 5000) * processTime;
            cnt = (long) Math.ceil(ratio.getWaterOrderInputQty() / 5000);
        } else if (productId.equals("P003") || productId.equals("P004")) {
            workProcessTime = ratio.getJellyOrderInputQty() / 5000 * processTime;
            cnt = (long) Math.ceil(ratio.getJellyOrderInputQty() / 5000);
        }
        // 추가 작업
        return new AbstractMap.SimpleEntry<>(workProcessTime, cnt);
    }

    private AbstractMap.SimpleEntry<Double, Long> process10(long processTime, long capa, long cnt, double workProcessTime) {
        System.out.println("inM-process10");

        workProcessTime = (long) (ratio.getOrderInput() / capa) * processTime;
        cnt = 1;
        // 추가 작업
        return new AbstractMap.SimpleEntry<>(workProcessTime, cnt);
    }

    //ArrayList<LocalDateTime> processTimeList = new ArrayList<>();










    public void insertArray(int cnt, LocalDateTime currentTime){


    }



    //현재시간을 고정 시간으로 설정
    private LocalDateTime setTime(LocalDateTime currentTime, int day, int hour) {
        currentTime = currentTime.plusDays(day).withHour(hour).withMinute(0).withSecond(0);
        return currentTime;
    }



    //요일 계산
    private LocalDateTime dayCheck(LocalDateTime currentTime){
        System.out.println("요일계산 커런트 - "+currentTime);
        long workDay = ordersRepository.findWorkDay(currentTime);                     //요일 출력
        System.out.println("workDay - "+ workDay);
        long currTime = Integer.parseInt(ordersRepository.findWorkTime(currentTime));
        System.out.println("currTime - "+ currTime);

        //월(2) 화(3) 수(4) 목(5) 금(6) 토(7) 일(1)

        if(workDay == Weekday.SATURDAY){
            currentTime = setTime(currentTime, 2, 9);                       //토요일이면 +2일 9:00
        }
        else if(workDay == Weekday.SUNDAY){
            currentTime = setTime(currentTime, 1,9);                        //일요일이면 +1일 9:00
        }
        else if(workDay == Weekday.FRIDAY){
            if(currTime >= 180000){                                                   //금요일 18시 이후
                currentTime = setTime(currentTime, 3,9);
            }
        }else{
            System.out.println("currentTime(if안) - "+currentTime);
            return currentTime;
        }
        System.out.println("currentTime(if밖) - "+currentTime);
        return currentTime;
    }

    //각 공정별 완료시간 ( 요일계산 -> 자동작업시간(공정)
    //                          -> 수동작업시간(공정) = setfinishdate(전공정의 끝난시간 = 다음공정의 시작시간)


    //자동 작업 시간 계산(원료계량, 식힘, 포장 제외)
    private LocalDateTime autoTimeCheck(LocalDateTime currentTime, long leadTime, long workTime){
        //leadTime: 각 공정별로 계산된 리드타임
        //workTime: 각 공정별로 계산된 작업시간

        long totalTime = leadTime + workTime;
        System.out.println("자동leadTime - "+leadTime);
        System.out.println("자동workTime - "+workTime);
        System.out.println("자동totalTime - "+totalTime);

        currentTime = dayCheck(currentTime);
        LocalDateTime totalWorkTime;

        //현재시간에 leadTime을 더한 시간을 %H$S%I 형태로 나타낸 것 ex) 13:50 -> 135000
        long addLeadTime = Integer.parseInt(ordersRepository.findWorkTime(currentTime.plusMinutes(leadTime)));
        System.out.println("자동addLeadTime = " + addLeadTime);

        //현재시간을 %H%S%I 형태로 나타낸 것
        long currTime = Integer.parseInt(ordersRepository.findWorkTime(currentTime));
        System.out.println("자동currTime = " +currTime);



        if((addLeadTime >= 90000 && addLeadTime < 120000)
                || (addLeadTime >= 130000 && addLeadTime < 180000)){

            //점심시간 퇴근시간 제외
            if(currTime >= 120000 && currTime < 130000){
                currentTime = setTime(currentTime, 0,13);
            }
            else if(currTime >= 180000 && currTime <= 235959){
                currentTime = setTime(currentTime, 1,9);
            }
            else if(currTime < 90000){
                currentTime = setTime(currentTime,0,9);
            }else{
                currentTime = dayCheck(currentTime);
            }

        }
        else if(addLeadTime >= 120000 && addLeadTime < 130000){
            currentTime = setTime(currentTime, 0,13);
        }

        else if(addLeadTime >= 180000 && addLeadTime <= 235959){
            currentTime = setTime(currentTime, 1,9);
        }

        else if(addLeadTime < 90000){
            currentTime = setTime(currentTime, 0,9);
        }

        totalWorkTime = dayCheck(currentTime.plusMinutes(totalTime));

        return currentTime;

    }

    //수작업 시간 계산
    private LocalDateTime selfTimeCheck(LocalDateTime currentTime, long leadTime, long workTime){
        //leadTime: 각 공정별로 계산된 리드타임
        //workTime: 각 공정별로 계산된 작업시간

        long totalTime = leadTime + workTime;
        System.out.println("leadTime - "+leadTime);
        System.out.println("workTime - "+workTime);
        System.out.println("totalTime - "+totalTime);
        currentTime = dayCheck(currentTime);
        System.out.println("셀프커런트 - "+currentTime);
        LocalDateTime totalWorkTime = null;

        //현재시간에 전체 작업 시간을 더한 시간을 %H$S%I 형태로 나타낸 것 ex) 13:50 -> 135000
        long addTime = Integer.parseInt(ordersRepository.findWorkTime(currentTime.plusMinutes(totalTime)));
        System.out.println("addTime = " + addTime);

        //현재시간에 리드타임을 더한 시간을 %H$S%I 형태로 나타낸 것
        long addLeadTime = Integer.parseInt(ordersRepository.findWorkTime(currentTime.plusMinutes(leadTime)));
        System.out.println("셀프 addLeadTime - "+addLeadTime);

        //현재시간을 %H%S%I 형태로 나타낸 것
        long currTime = Integer.parseInt(ordersRepository.findWorkTime(currentTime));
        System.out.println("currTime = " +currTime);

        //점심시간 퇴근시간 제외
        if(currTime >= 120000 && currTime < 130000){
            currentTime = setTime(currentTime, 0,13);
        }
        else if(currTime >= 180000 && currTime <= 235959){
            currentTime = setTime(currentTime, 1,9);
        }
        else if(currTime < 90000){
            currentTime = setTime(currentTime, 0,9);
        }
        else{
            currentTime = currentTime; //currtime 101000 addLeadTime 10300 addtime 13:30:00
        }



        if (addTime >= 120000 && addTime < 130000) {
            currentTime = currentTime.plusMinutes(60);
            //totalWorkTime = currentTime.plusMinutes(60+totalTime);            ;
            System.out.println("셀프10번 - " +totalWorkTime);
        }else if(addTime >= 180000){
            if(addLeadTime > 180000){
                currentTime = setTime(currentTime,1,9);
                totalWorkTime = currentTime.plusMinutes(totalTime);
            }else{
                currentTime = currentTime.plusDays(1).withHour(9);
                totalWorkTime = currentTime.plusMinutes(totalTime).plusDays(1).withHour(9);
                long workDay = ordersRepository.findWorkDay(totalWorkTime);
                if(workDay == Weekday.FRIDAY){
                    currentTime = currentTime.plusDays(3).withHour(9);
                    totalWorkTime = currentTime.plusMinutes(totalTime).plusDays(3).withHour(9);
                }
//                    Long toTime = (long) Integer.parseInt(ordersRepository.findWorkTime(totalWorkTime));
//                    System.out.println("셀프투타임 - "+toTime);
//                    if((toTime >= 90000 && toTime < 120000) || (toTime >= 130000 && toTime < 180000)){
//
//                        totalWorkTime = totalWorkTime;
//                        System.out.println("셀프토탈(이프이프_) - "+totalWorkTime);
//                    }else{
//                    ////////////////////////여기수정/////////////////////////
//                    totalWorkTime = currentTime.plusMinutes(totalTime).plusDays(3).withHour(9);//plusDay가 4가되야 맞지않나?
//                    System.out.println("셀프totalWorkTime(if안) - "+totalWorkTime);
//                    }

            }
        }


        totalWorkTime = currentTime.plusMinutes(totalTime);

        System.out.println("셀프토탈워크타임(if밖) - "+totalWorkTime);
        return currentTime;
    }

    public ArrayList<String> selectProcess(String productId) {
        //productId = "p001";
        System.out.println("Here - " + productId);

        Routing rout = routingRepository.findByProductId(productId);

        ArrayList<String> processList = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();

        temp.add(rout.getProcessNo1());
        temp.add(rout.getProcessNo2());
        temp.add(rout.getProcessNo3());
        temp.add(rout.getProcessNo4());
        temp.add(rout.getProcessNo5());
        temp.add(rout.getProcessNo6());
        temp.add(rout.getProcessNo7());
        temp.add(rout.getProcessNo8());
        temp.add(rout.getProcessNo9());
        temp.add(rout.getProcessNo10());

        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i) != null && !temp.get(i).isEmpty()) {
                processList.add(temp.get(i));
            }
        }
        return processList;
    }

    public void find(ProductionPlan productionPlan) {
        this.productionPlan = productionPlan;
    }

}
