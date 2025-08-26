package com.doanth.auth_service.repository;

import com.doanth.auth_service.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepo extends JpaRepository<Service, Long> {
    Optional<Service> findByServiceName(String serviceName);
}
