package b3.spl.splb.Services;

import b3.spl.splb.model.AppUser;
import b3.spl.splb.model.Car;
import b3.spl.splb.model.Role;

import java.util.List;

public interface AppUserService {
    AppUser getUser(String email);
    AppUser saveUser(AppUser user);
    void updateUser(AppUser updatedUser, AppUser user);
    Role saveRole(Role role);
    boolean addRoleToAppUser(String email,  String rolName);
    boolean setBannedUser(String email, Boolean banned);
    boolean setBannedProvider(String email, Boolean banned);
    boolean addCarToUser(Long carId, Long userId);
    List<AppUser> getUsers();
    List<Car> getUserCars(String email);
    boolean checkIfValidOldPassword(AppUser user, String oldPassword);
    boolean changeUserPassword(AppUser appUser, String newPassword);
    void deleteUser(AppUser user);
}
