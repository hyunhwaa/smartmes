package mes.smartmes.service;


import com.querydsl.core.BooleanBuilder;
import mes.smartmes.entity.Process;
import mes.smartmes.entity.QProcess;
import mes.smartmes.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProcessService {
    @Autowired
    private final ProcessRepository processRepository;
    public List<Process> getAllProcess() {
        return processRepository.findAll();
    }

    @Autowired
    public ProcessService(ProcessRepository processRepository) {
        this.processRepository =processRepository;

    }

    // 다중검색

    @Transactional
    public List<Process> searchProcess(String processNO, String processName,String processDivision) {
        QProcess qProcess = QProcess.process;
        BooleanBuilder builder = new BooleanBuilder();


        if (processNO != null && processNO != "") {
            builder.and(qProcess.processNO.contains(processNO)); //출하번호
        }


        if (processName != null && processName != "") {
            builder.and(qProcess.processName.contains(processName)); // 거래처
        }

        if (processDivision != null && processDivision != "") {
            builder.and(qProcess.processDivision.contains(processDivision)); // 거래처
        }



        return (List<Process>) processRepository.findAll(builder);

    }

}
