package ma.hamza.backendstudentsapp.repository;


import ma.hamza.backendstudentsapp.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole,Long> {
    AppRole findByName(String name);
}
