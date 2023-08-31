package mes.smartmes.service;

import lombok.RequiredArgsConstructor;
import mes.smartmes.entity.Routing;
import mes.smartmes.repository.RoutingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutingService {

    private final RoutingRepository routingRepository;
    public List<Routing> getAllRouting() {
        return routingRepository.findAll();
    }






}
