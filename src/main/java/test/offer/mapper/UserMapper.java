package test.offer.mapper;

import test.offer.model.UserEntity;
import test.offer.model.UserResource;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

      UserResource carEntityToCarResource(UserEntity carEntity);

      UserEntity carResourceToCarEntity(UserResource carResource);

}
