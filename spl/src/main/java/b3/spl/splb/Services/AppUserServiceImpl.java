package b3.spl.splb.Services;

import b3.spl.splb.model.AppUser;
import b3.spl.splb.model.Role;
import b3.spl.splb.repository.AppUserRepo;
import b3.spl.splb.repository.ParkingLotRepo;
import b3.spl.splb.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    private final AppUserRepo appUserRepo;
    private final RoleRepo roleRepo;
    private final ParkingLotRepo parkingLotRepo;

    private final PasswordEncoder passwordEncoder;

    @Override //am rescris ca sa caute dupa emali in baza de date
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        AppUser appUser = appUserRepo.findByEmail(email);
        if(appUser == null){
            log.error("User not found in database");
            throw new UsernameNotFoundException("User not found in the database");
        }
        else {
            log.info("User found in database : {}", email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(role ->{authorities.add(new SimpleGrantedAuthority(role.getName()));});
        return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), authorities);
    }

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("Saving new user {} to the database", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToAppUser(String email, String rolName) {
        log.info("Adding role {} to user {}", rolName, email);
        AppUser appUser = appUserRepo.findByEmail(email);
        Role role = roleRepo.findByName(rolName);
        appUser.getRoles().add(role);
    }

    @Override
    public AppUser getUser(String email) {
        log.info("Fetching user {}", email);
        return appUserRepo.findByEmail(email);
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("Fetching all users");
        return appUserRepo.findAll();
    }





}
