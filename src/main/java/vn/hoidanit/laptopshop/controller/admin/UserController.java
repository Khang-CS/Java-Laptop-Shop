package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {

    // // Dependency Injection
    private final UserService userService;
    private final UploadService uploadService;

    public UserController(UserService userService, UploadService uploadService) {
        this.userService = userService;
        this.uploadService = uploadService;
    }

    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUser();
        System.out.println("check users: " + users);
        model.addAttribute("users", users);
        return "admin/user/show";
    }

    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }

    @RequestMapping("/admin/user/update/{id}")
    public String getUserUpdatePage(Model model, @PathVariable long id) {
        User oldUser = this.userService.getUserById(id);
        model.addAttribute("updateUser", oldUser);
        return "admin/user/update";
    }

    @RequestMapping(value = "/admin/user/update", method = RequestMethod.POST)
    public String updateUser(Model model, @ModelAttribute("updateUser") User user) {
        User currentUser = this.userService.getUserById(user.getId());

        if (user != null) {
            currentUser.setAddress(user.getAddress());
            currentUser.setPassword(user.getPassword());
            currentUser.setFullName(user.getFullName());
            currentUser.setPhone(user.getPhone());
            currentUser.setEmail(user.getEmail());

            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }

    @RequestMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        return "/admin/user/delete";
    }

    @PostMapping("/admin/user/delete/action/{id}")
    public String deleteUser(@PathVariable long id) {
        // TODO: process POST request
        this.userService.handleDeleteUser(id);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/create") // GET
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping("/admin/user/create")
    public String createUserPage(
            Model model,
            @ModelAttribute("newUser") User hoidanit,
            @RequestParam("hoidanitFile") MultipartFile file) {

        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");

        // this.userService.handleSaveUser(hoidanit);
        return "redirect:/admin/user";
    }
}
