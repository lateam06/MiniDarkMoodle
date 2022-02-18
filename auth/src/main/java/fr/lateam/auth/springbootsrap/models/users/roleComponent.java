package fr.lateam.auth.springbootsrap.models.users;

import fr.lateam.auth.springbootsrap.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class roleComponent {

    @Autowired
    RoleRepository roleRepository;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        Role t = roleRepository.findByName(ERole.ROLE_TEACHER).orElse(new Role(ERole.ROLE_TEACHER));
        Role s = roleRepository.findByName(ERole.ROLE_STUDENT).orElse(new Role(ERole.ROLE_STUDENT));
        Role a = roleRepository.findByName(ERole.ROLE_ADMIN).orElse(new Role(ERole.ROLE_ADMIN));

        roleRepository.save(t);
        roleRepository.save(a);
        roleRepository.save(s);


    }



}
