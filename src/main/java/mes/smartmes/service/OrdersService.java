package mes.smartmes.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mes.smartmes.dto.OrdersDTO;
import mes.smartmes.dto.Ratio;
import mes.smartmes.dto.Weekday;
import mes.smartmes.entity.Orders;
import mes.smartmes.entity.Routing;
import mes.smartmes.repository.OrdersRepository;
import mes.smartmes.repository.RoutingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


@Service
@Transactional
@RequiredArgsConstructor
public class OrdersService {

    //필드 선언

    private final OrdersRepository ordersRepository;
    private final RoutingRepository rr;

    Scanner sc = new Scanner(System.in);
    double totalTime = 0;
    double workLeadTime = 0; // 공정별 리드타임
    double workProcessTime = 0; // 공정별 소요 시간

    // 현재시간 설정
    LocalDateTime totalProcessTime = LocalDateTime.now();

    // 스트링 타임 processList 배열 생성
    List<String> processList = new ArrayList<>();

    // 스트링 타입 temp 배열 생성
    List<String> temp = new ArrayList<>();


    //조회
    public String selectOrderNo() {
        String OrderIntNo = ordersRepository.findByOrderNo();
        return OrderIntNo;
    }

/*    public Orders updateOrderByOrderNo(String orderNo, OrdersDTO ordersDTO) {
    }*/

    // 삭제
    public int deleteByOrderNo(String orderNo) {
        return ordersRepository.deleteByOrderNo(orderNo);
    }



    public String processSetting(String productId) {


        // 라우팅에 productId를 받아오고
        Routing rout = rr.findByProductId(productId);

        // temp 배열에 Routing entity에 저장된 값들을 저장한다.
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
            // 하나씩 돌면서 temp를 검사하고 값이 널이 아니면
            if (temp.get(i) != null) {
                processList.add(temp.get(i)); // processList 에 값 저장
                System.out.println("temp 확인" + temp.get(i));
            }
        }

        System.out.println("총 ProcessTime에 workLeadTime의 시간을 분으로 저장 \n" + totalProcessTime.plusMinutes((long) workLeadTime) + "더해진 시간 : " + workLeadTime);

        return productId;

    }

    public void updateOrder(Orders orders) {
        ordersRepository.save(orders);
    }



    // 공정 1~6번
//    public long selectProcessOneToSix(String productId) {
//
//        System.out.println(productId);
//
//        for (int i = 0; i < processList.size(); i++) {
//            String processNo = processList.get(i);  // processNo에 저장
//            double leadTime = ordersRepository.findLeadTime(processNo); // processNo에 해당하는 리드타임을 찾아 저장
//            double processTime = ordersRepository.findProcessTime(processNo);
//            System.out.println("프로세스타임 : " + processTime);
//            long capa = ordersRepository.findCapa(processNo);
//
//
//            // process01 : 원료 계량
//            if (processList.get(i).equals("process01")) {
//                System.out.println("여기 들어오나?");
//
//                workLeadTime = leadTime;
//                workProcessTime = processTime;
//                System.out.println("여긴 들어오나?" + processList.toString());
//                System.out.println(processList.get(i));
//
//                // process02 : 전처리
//            } else if (processList.get(i).equals("process02")) {
//                System.out.println("여긴 안오는듯?" + processList.get(i) + productId);
//                if (productId.equals("p001")) {
//                    System.out.println("여기인가? " + productId);
//
//                    // 양배추즙 전처리 리드타임 계산
//                    workLeadTime = (long) Math.ceil(Ratio.cabbageInputQty / (double) 1000) * leadTime;
//                    System.out.println("양배추즙 리드타임 : " + workLeadTime + "분");
//
//                    // 양배추즙 전처리 워크타임 계산
//                    workProcessTime = (Ratio.cabbageInputQty / 1000) * processTime;
//
//                    System.out.println("양배추즙 전처리 워크타임 : " + workProcessTime + "분   " + processTime + " " + Ratio.cabbageInputQty);
//
//                } else if (productId.equals("p002")) {
//
//                    // 흑마늘 전처리 리드타임 계산
//                    workLeadTime = (long) Math.ceil(Ratio.garlicInputQty / (double) 1000) * leadTime;
//                    System.out.println("흑마늘즙 전처리 리드타임 계산 : " + workProcessTime + "분");
//                    // 흑마늘 전처리 워크타임 계산
//                    workProcessTime = (Ratio.garlicInputQty / 1000) * processTime;
//                    System.out.println("흑마늘즙 전처리 워크타임 : " + workProcessTime + "분   " + processTime + " " + Ratio.garlicInputQty);
//
//                }
//
//                // process03 : 추출
//            } else if (processList.get(i).equals("process03")) {
//                if (productId.equals("p001")) {  // 상품이 양배추즙이면
//
//                    //양배추즙 추출 리드타임
//                    workLeadTime = (long) Math.ceil((Ratio.cabbageWater / (double) 2000) * leadTime);
//                    System.out.println("양배추즙 추출 리드타임 : " + workLeadTime + "분");
//                    //양배추즙 추출 워크타임
//                    workProcessTime = (Ratio.cabbageWater) / capa * processTime;
//                    System.out.println("양배추즙 추출 워크타임 : " + workProcessTime + "분");
//
//                } else if (productId.equals("p002")) {
//
//                    //흑마늘즙 추출 리드타임
//                    workLeadTime = (long) Math.ceil((Ratio.blackGarlicWater / (double) 2000) * leadTime);
//                    System.out.println("흑마늘즙 추출 리드타임 : " + workLeadTime + "분");
//                    //흑마늘즙 추출 워크타임
//                    workProcessTime = (Ratio.blackGarlicWater / capa * processTime);
//                    System.out.println("흑마늘즙 추출 워크타임 : " + workProcessTime + "분  " + processTime + " " + Ratio.blackGarlicWater);
//
//                }
//
//                // process 04 : 혼합 및 살균 _즙
//            } else if (processList.get(i).equals("process04")) {
//                // 양배추즙 혼합 및 살균
//                if (productId.equals("p001")) {
//                    workProcessTime = (Ratio.cabbageWaterOutput / (double) 2000) * processTime;
//                    System.out.println("양배추즙 혼합 및 살균 : " + workProcessTime + "분  " + processTime + " " + Ratio.cabbageWaterOutput);
//                }
//                // 흑마늘즙 혼합 및 살균
//                else if (productId.equals("p002")) {
//                    workProcessTime = (Ratio.blackGarlicOutput / (double) 2000) * processTime;
//                    System.out.println("흑마늘즙 혼합 및 살균 : " + workProcessTime + "분  " + processTime + " " + Ratio.blackGarlicOutput);
//                }
//
//                // process05 : 혼합 및 살균_젤리
//            } else if (processList.get(i).equals("process05")) {
//
//                // 젤리 혼합 및 살균 리드타임
//                workLeadTime = (long) Math.ceil(Ratio.jellyInputQty / (double) 2000000);
//                System.out.println("젤리 혼합 및 살균 리드타임 : " + workLeadTime);
//
//                // 젤리 혼합 및 살균 워크타임
//                workProcessTime = (Ratio.jellyOrderInputQty / (double) 2000000) * processTime;
//                System.out.println("젤리 혼합 및 살균 워크타임 : " + workProcessTime + "분  " + processTime + " " + Ratio.jellyOrderInputQty);
//
//                // process06 : 식힘
//            } else if (processList.get(i).equals("process06")) {
//                //  하루 + 9 시간  - 확인
//                totalProcessTime = totalProcessTime.plusDays(1).withHour(9).withMinute(0).withSecond(0);
//                System.out.println("식힘 시간" + totalProcessTime);
//
//            }
//
//            // 여기서 묶고
//
//        }
//
//        return (long)totalTime;
//    }
//
//    public long selectProcessSvenToEight(String productId) {
//
//
//        System.out.println(productId);
//
//        for (int i = 0; i < processList.size(); i++) {
//            String processNo = processList.get(i);  // processNo에 저장
//            double leadTime = ordersRepository.findLeadTime(processNo); // processNo에 해당하는 리드타임을 찾아 저장
//            double processTime = ordersRepository.findProcessTime(processNo);
//            System.out.println("세븐 투 에잇 프로세스타임 : " + processTime);
//            long capa = ordersRepository.findCapa(processNo);
//
//
//            // process07 : 충진_즙
//            if (processList.get(i).equals("process07")) {
//
//                // 즙 류 충진 리드타임
//                workLeadTime = leadTime;
//                System.out.println("즙 류 충진 리드타임 : " + workLeadTime + "분");
//
//                // 즙 류 충진 워크타임
//                workProcessTime = (Ratio.waterOrderInputQty / 3500) * processTime;
//                System.out.println("즙 류 충진 워크타임 : " + workProcessTime + "분  " + processTime + " " + Ratio.waterOrderInputQty);
//
//                //process08 : 충진 젤리
//            } else if (processList.get(i).equals("process08")) {
//
//                // 젤리류 충진 리드타임
//                workLeadTime = leadTime;
//                System.out.println("젤리류 충진 리드타임 : " + workLeadTime + "분");
//
//                //젤리류 충진 워크타임
//                workProcessTime = (Ratio.jellyOrderInputQty / 3000) * processTime;
//                System.out.println("젤리류 충진 워크타임 : " + workProcessTime + "분 " + processTime + " " + Ratio.jellyOrderInputQty);
//            }
//        }
//        return (long) totalTime;
//    }
//
//
//
//
//
//
//    public long selectProcessNineToTen(String productId) {
//
//        // process09 : 검사
//        for (int i = 0; i < processList.size(); i++) {
//
//            String processNo = processList.get(i);  // processNo에 저장
//            double leadTime = ordersRepository.findLeadTime(processNo); // processNo에 해당하는 리드타임을 찾아 저장
//            double processTime = ordersRepository.findProcessTime(processNo);
//            System.out.println("세븐 투 에잇 프로세스타임 : " + processTime);
//            long capa = ordersRepository.findCapa(processNo);
//
//            if (processList.get(i).equals("process09")) {
//
//                // 즙류 검사
//            } else if (productId.equals("p001") || productId.equals("p002")) {
//
//                // 즙류 검사 리드타임
//                workLeadTime = (long) Math.ceil(Ratio.waterOrderInputQty / (double) 5000) * leadTime;
//                System.out.println("즙류 검사 리드타임 : " + workLeadTime);
//
//                // 즙류 검사 워크타임
//                workProcessTime = (Ratio.waterOrderInputQty / 5000) * processTime;
//                System.out.println("즙류 검사 워크타임 : " + workProcessTime + "분  " + processTime + " " + Ratio.waterOrderInputQty);
//
//                // 젤리류 검사
//            } else if (productId.equals("p003") || productId.equals("p004")) {
//                // 젤리류 검사 리드타임
//                workLeadTime = (long) Math.ceil(Ratio.jellyOrderInputQty / (double) 5000) * leadTime;
//                System.out.println("젤리류 검사 리드타임 : " + workLeadTime);
//
//                // 젤리류 검사 워크타임
//                workProcessTime = (Ratio.jellyOrderInputQty / 5000 * processTime);
//                System.out.println("젤리류 검사 워크타임 : " + workProcessTime);
//
//                // process10 : 포장
//            } else if (processList.get(i).equals("process10")) {
//
//                // 포장 리드타임
//                workLeadTime = leadTime;
//                System.out.println("포장 리드타임 : " + workLeadTime + "분");
//
//                // 포장 워크타임
//                workProcessTime = (Ratio.orderInput / capa);      // Ratio.orderInput 임의 지정 값 = 666 박스
//                System.out.println("포장 워크타임 : " + workProcessTime + "분  " + processTime + " " + Ratio.orderInput);
//                System.out.println("프로세스10 에서 capa는?  " + capa);
//
//            }
//        }
//        System.out.println("토탈시간 : " + totalTime);
//        return (long)totalTime;
//    }
//
//
//
//    //현재시간을 고정 시간으로 설정
//    private LocalDateTime setTime(LocalDateTime currentTime, int day, int hour) {
//        currentTime = currentTime.plusDays(day).withHour(hour).withMinute(0).withSecond(0);
//        return currentTime;
//    }
//
//    //요일 계산
//    private LocalDateTime dayCheck(LocalDateTime currentTime){
//
//        long workDay = ordersRepository.findWorkDay(currentTime);                     //요일 출력
//
//        //월(2) 화(3) 수(4) 목(5) 금(6) 토(7) 일(1)
//
//        if(workDay == Weekday.SATURDAY){
//            currentTime = setTime(currentTime, 2, 9);                       //토요일이면 +2일 9:00
//        }
//        else if(workDay == Weekday.SUNDAY){
//            currentTime = setTime(currentTime, 1,9);                        //일요일이면 +1일 9:00
//        }
//        return currentTime;
//    }
//
//
//    //자동 작업 시간 계산
//    public LocalDateTime autoTimeCheck(LocalDateTime currentTime, long leadTime, long workTime){
//        //leadTime: 각 공정별로 계산된 리드타임
//        //workTime: 각 공정별로 계산된 작업시간
//
//        long totalTime = leadTime + workTime;
//        currentTime = dayCheck(currentTime);
//
//        //현재시간에 leadTime을 더한 시간을 %H$S%I 형태로 나타낸 것 ex) 13:50 -> 135000
//        long addLeadTime = Integer.parseInt(ordersRepository.findWorkTime(currentTime.plusMinutes(leadTime)));
//        System.out.println("addLeadTime = " + addLeadTime);
//
//        //현재시간을 %H%S%I 형태로 나타낸 것
//        long currTime = Integer.parseInt(ordersRepository.findWorkTime(currentTime));
//        System.out.println("currTime = " +currTime);
//
//
//
//        if((addLeadTime >= 900000 && addLeadTime < 120000)
//                || (addLeadTime >= 130000 && addLeadTime < 180000)){
//
//            if(currTime >= 120000 && currTime < 130000){
//                currentTime = setTime(currentTime, 0,13);
//            }
//            else if(currTime >= 180000 && currTime <= 235959){
//                currentTime = setTime(currentTime, 1,9);
//            }
//            else if(currTime < 90000){
//                currentTime = setTime(currentTime,0,9);
//            }else{
//                currentTime = currentTime;
//            }
//
//        }
//        else if(addLeadTime >= 120000 && addLeadTime < 130000){
//            currentTime = setTime(currentTime, 0,13);
//        }
//
//        else if(addLeadTime >= 180000 && addLeadTime <= 235959){
//            currentTime = setTime(currentTime, 1,9);
//        }
//
//        else if(addLeadTime < 90000){
//            currentTime = setTime(currentTime, 0,9);
//        }
//
//        currentTime = dayCheck(currentTime.plusMinutes(totalTime));
//
//        return currentTime;
//
//    }
//
//    //수작업 작업 시간 계산
//    private LocalDateTime selfTimeCheck(LocalDateTime currentTime, int leadTime, int workTime){
//
//        return null;
//    }

}
