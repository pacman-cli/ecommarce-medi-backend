package com.example.ecommerce.audit.mapper;

import com.example.ecommerce.audit.dto.response.ActivityLogResponse;
import com.example.ecommerce.audit.dto.response.AuditLogResponse;
import com.example.ecommerce.audit.dto.response.LoginHistoryResponse;
import com.example.ecommerce.audit.entity.ActivityLog;
import com.example.ecommerce.audit.entity.AuditLog;
import com.example.ecommerce.audit.entity.LoginHistory;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct mapper for converting between audit entities and response DTOs.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface AuditMapper {

    AuditLogResponse toAuditLogResponse(AuditLog auditLog);

    ActivityLogResponse toActivityLogResponse(ActivityLog activityLog);

    LoginHistoryResponse toLoginHistoryResponse(LoginHistory loginHistory);
}
