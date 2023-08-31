package mes.smartmes.service;

import lombok.RequiredArgsConstructor;
import mes.smartmes.dto.Weekday;
import mes.smartmes.entity.*;
import mes.smartmes.repository.IngredientInputRepository;
import mes.smartmes.repository.IngredientStockRepository;
import mes.smartmes.repository.PorderRepository;
import mes.smartmes.repository.ProdPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class PorderService {

    private final PorderRepository porderRepository;
    private final ProdPlanRepository prodPlanRepository;

//    @Autowired
//    WorkOrderService workOrderService;
    @Autowired
    LotService lotService;


    public List<Porder> selectList() {
        return porderRepository.findAll();
    }


    public List<Porder> findSearch(java.util.Date startDate, java.util.Date endDate, String porderStatus, String supplierId) {
        return porderRepository.findSearch(startDate, endDate, porderStatus, supplierId);

    }

    // 은영

    //발주 내역 리스트
    public List<Porder> selectPorderList() {
        return porderRepository.findAll();
    }

    //현재시간을 고정 시간으로 설정
    private LocalDateTime setTime(LocalDateTime currentTime, int day, int hour) {
        currentTime = currentTime.plusDays(day).withHour(hour).withMinute(0).withSecond(0);
        return currentTime;
    }

    //입고 예정 일자 생성
    public LocalDateTime selectInputDate(String porderNo) {
        System.out.println("여기야여기");
        int inputTime = Integer.parseInt(porderRepository.selectTime(porderNo));            //발주 등록 시간
        System.out.println("inputTime = " + inputTime);

        int inputDay = porderRepository.selectDay(porderNo);                                //발주 등록 요일

        System.out.println("inputDay = " + inputDay);

        LocalDateTime inputDate = porderRepository.selectDate(porderNo);                    //실제 발주 날짜(발주 등록 날짜랑 다름)

        System.out.println("inputDate = " + inputDate);

        LocalDateTime inputIngreDate = inputDate;                                           //입고날짜

        //12시 이후 주문 시 발주 날짜는 +1일
        System.out.println(inputDay);
        if (inputTime > 120000) {
            inputDate = inputDate.plusDays(1);
            inputDay = porderRepository.checkDay(inputDate);
            if (inputDay == Weekday.SATURDAY) {
                inputDate = setTime(inputDate, 2, 9);
                inputDay = porderRepository.checkDay(inputDate);
            } else {
                inputDay = inputDay;
            }
        }

        String emerYn = porderRepository.emergencyYn(porderNo);                             //긴급 입고 여부 확인
        System.out.println("---------------------");
        System.out.println(inputDay);
        System.out.println(inputDate);
        System.out.println("---------------------");
        System.out.println("emerYn = " + emerYn);

        String ingre = porderRepository.selectIngreId(porderNo);


        // 입고 예정 시간


        // 재고 관리 시  입고 예정 시간 이후면 발주 한거에서 현재시간이 입고예정시간 보다 크거나 같으면 입고 대기에서 입고완료로 바뀌고
        // 자재 입고테이블로 인서트문 넣고?               // 입고테이블 입고 내역
        // 재고 관리에서 인서트??                        //  현재 재고


        if (ingre.equals("양배추") || ingre.equals("흑마늘") || ingre.equals("파우치") || ingre.equals("스틱파우치") || ingre.equals("포장Box")) {
            if (emerYn.equals("N")) {
                inputDate = inputDate.plusDays(2);
                inputDay = porderRepository.checkDay(inputDate);
                System.out.println("+++++++++++++++++");
                System.out.println(inputDay);
                System.out.println("+++++++++++++++++");
                if (inputDay == Weekday.MONDAY || inputDay == Weekday.WEDNESDAY || inputDay == Weekday.FRIDAY) {
                    inputIngreDate = setTime(inputDate, 0, 10);
                } else if (inputDay == Weekday.SUNDAY || inputDay == Weekday.TUESDAY || inputDay == Weekday.THURSDAY) {
                    inputIngreDate = setTime(inputDate, 1, 10);
                } else if (inputDay == Weekday.SATURDAY) {
                    inputIngreDate = setTime(inputDate, 2, 10);
                }
            } else {
                return inputIngreDate;
            }


        } else if (ingre.equals("석류농축액") || ingre.equals("매실농축액") || ingre.equals("콜라겐")) {
            if (emerYn.equals("N")) {
                inputDate = inputDate.plusDays(3);
                inputDay = porderRepository.checkDay(inputDate);

                System.out.println("인풋데이 = " + inputDay);
                System.out.println("+++++++++++++++++");
                System.out.println(inputDate);
                System.out.println(inputDay);
                System.out.println("+++++++++++++++++");

                if (inputDay == Weekday.MONDAY || inputDay == Weekday.WEDNESDAY || inputDay == Weekday.FRIDAY) {
                    System.out.println("여기????????1111111111111111");
                    inputIngreDate = setTime(inputDate, 0, 10);
                } else if (inputDay == Weekday.SUNDAY || inputDay == Weekday.TUESDAY || inputDay == Weekday.THURSDAY) {
                    System.out.println("여기????????22222222222222222");
                    inputIngreDate = setTime(inputDate, 1, 10);
                } else if (inputDay == Weekday.SATURDAY) {
                    System.out.println("여기????????33333333333333");
                    inputIngreDate = setTime(inputDate, 2, 10);
                }
            } else {
                return inputIngreDate;
            }
        }


        System.out.println("ingre = " + ingre);
        System.out.println(inputIngreDate);
        return inputIngreDate;
    }

    public LocalDateTime prodPlanDay(String planNo) {
        System.out.println("포더서비스 플랜노 : " + planNo);
        ProductionPlan pp = prodPlanRepository.selectPlanNo(planNo);
        int inputTime = Integer.parseInt(prodPlanRepository.selectTime(planNo));            //발주 등록 시간
        System.out.println("inputTime = " + inputTime);

        int inputDay = prodPlanRepository.selectDay(planNo);                                //발주 등록 요일

        System.out.println("inputDay = " + inputDay);

        LocalDateTime inputDate = prodPlanRepository.selectDate(planNo);                    //실제 발주 날짜(발주 등록 날짜랑 다름)

        System.out.println("inputDate = " + inputDate);

        LocalDateTime inputIngreDate = inputDate;                                           //입고날짜

        //12시 이후 주문 시 발주 날짜는 +1일
        System.out.println(inputDay);
        if (pp.getProductId().equals("P001") || pp.getProductId().equals("P002")) {
            if (inputTime > 120000) {
                inputDate = inputDate.plusDays(3);
                inputDay = porderRepository.checkDay(inputDate);
                if (inputDay == Weekday.SATURDAY) {
                    inputDate = setTime(inputDate, 2, 10);
                    inputDay = porderRepository.checkDay(inputDate);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.SUNDAY) {
                    inputDate = setTime(inputDate, 1, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.TUESDAY) {
                    inputDate = setTime(inputDate, 1, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.THURSDAY) {
                    inputDate = setTime(inputDate, 1, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.MONDAY) {
                    inputDate = setTime(inputDate, 0, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.WEDNESDAY) {
                    inputDate = setTime(inputDate, 0, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.FRIDAY) {
                    inputDate = setTime(inputDate, 0, 10);
                    inputIngreDate = inputDate;
                }
            } else {
                inputDate = inputDate.plusDays(2);
                inputDay = porderRepository.checkDay(inputDate);
                if (inputDay == Weekday.SATURDAY) {
                    inputDate = setTime(inputDate, 2, 10);
                    inputDay = porderRepository.checkDay(inputDate);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.SUNDAY) {
                    inputDate = setTime(inputDate, 1, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.TUESDAY) {
                    inputDate = setTime(inputDate, 1, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.THURSDAY) {
                    inputDate = setTime(inputDate, 1, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.MONDAY) {
                    inputDate = setTime(inputDate, 0, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.WEDNESDAY) {
                    inputDate = setTime(inputDate, 0, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.FRIDAY) {
                    inputDate = setTime(inputDate, 0, 10);
                    inputIngreDate = inputDate;
                }
            }
        }
        if (pp.getProductId().equals("P003") || pp.getProductId().equals("P004")) {
            if (inputTime > 150000) {
                inputDate = inputDate.plusDays(4);
                inputDay = porderRepository.checkDay(inputDate);
                if (inputDay == Weekday.SATURDAY) {
                    inputDate = setTime(inputDate, 2, 10);
                    inputDay = porderRepository.checkDay(inputDate);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.SUNDAY) {
                    inputDate = setTime(inputDate, 1, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.TUESDAY) {
                    inputDate = setTime(inputDate, 1, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.THURSDAY) {
                    inputDate = setTime(inputDate, 1, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.MONDAY) {
                    inputDate = setTime(inputDate, 0, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.WEDNESDAY) {
                    inputDate = setTime(inputDate, 0, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.FRIDAY) {
                    inputDate = setTime(inputDate, 0, 10);
                    inputIngreDate = inputDate;
                }
            } else{
                inputDate = inputDate.plusDays(3);
                inputDay = porderRepository.checkDay(inputDate);
                if (inputDay == Weekday.SATURDAY) {
                    inputDate = setTime(inputDate, 2, 10);
                    inputDay = porderRepository.checkDay(inputDate);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.SUNDAY) {
                    inputDate = setTime(inputDate, 1, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.TUESDAY) {
                    inputDate = setTime(inputDate, 1, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.THURSDAY) {
                    inputDate = setTime(inputDate, 1, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.MONDAY) {
                    inputDate = setTime(inputDate, 0, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.WEDNESDAY) {
                    inputDate = setTime(inputDate, 0, 10);
                    inputIngreDate = inputDate;
                } else if (inputDay == Weekday.FRIDAY) {
                    inputDate = setTime(inputDate, 0, 10);
                    inputIngreDate = inputDate;
                }
            }
        }
        return inputIngreDate;
    }


}


        //희람





