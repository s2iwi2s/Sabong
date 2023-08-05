package com.s2i.sabong.service.mapper;

import com.s2i.sabong.data.domain.UserEntity;
import com.s2i.sabong.service.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface UserMapper extends EntityMapper<UserDTO, UserEntity>{
}
