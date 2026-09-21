package com.petproject.term_paper.dto.mapping;

import com.petproject.term_paper.dto.AgentSummaryDTO;
import com.petproject.term_paper.dto.OwnerSummaryDTO;
import com.petproject.term_paper.dto.PropertyDTO;
import com.petproject.term_paper.entity.OwnerEntity;
import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropertyMapping {
    PropertyDTO toDTO(PropertyEntity property);

    default OwnerSummaryDTO toOwnerSummary(OwnerEntity owner) {
        if (owner == null) return null;
        String displayName = owner.getOwnerType() == null ? ""
                : switch (owner.getOwnerType()) {
                    case COMPANY -> owner.getCompanyName();
                    case INDIVIDUAL -> String.join(" ",
                            value(owner.getLastName()), value(owner.getFirstName()), value(owner.getMiddleName())).trim();
                };
        return new OwnerSummaryDTO(owner.getId(), owner.getOwnerType(), displayName, owner.getEmail(), owner.getPhone());
    }

    default AgentSummaryDTO toAgentSummary(UserEntity agent) {
        return agent == null ? null : new AgentSummaryDTO(agent.getId(), agent.getUsername(), agent.getEmail());
    }

    private static String value(String value) {
        return value == null ? "" : value;
    }
}
