package com.sigeg.service;

import com.sigeg.dto.response.DashboardResponse;
import java.time.LocalDateTime;
import java.util.UUID;

public interface DashboardService {
    DashboardResponse getDashboard(UUID restauranteId, LocalDateTime inicio, LocalDateTime fim, String emailUsuario);
}
