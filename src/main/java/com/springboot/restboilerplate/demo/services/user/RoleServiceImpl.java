package com.springboot.restboilerplate.demo.services.user;

import com.springboot.restboilerplate.demo.configs.constants.MessagesConstants;
import com.springboot.restboilerplate.demo.errors.ResourceNotFoundException;
import com.springboot.restboilerplate.demo.models.Role;
import com.springboot.restboilerplate.demo.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException(MessagesConstants.RESOURCES_NOT_FOUND+"role", name));
    }

    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role update(Role role, Role updatedRole) {
        role.setName(updatedRole.getName());
        return roleRepository.save(role);
    }

    @Override
    public void delete(Role role) {
        roleRepository.delete(role);
    }

    public Role createOrFind(String roleName){
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        if (optionalRole.isPresent()){
            return optionalRole.get();
        }
        Role role = new Role();
        role.setName(roleName);
        return create(role);


    }
}
