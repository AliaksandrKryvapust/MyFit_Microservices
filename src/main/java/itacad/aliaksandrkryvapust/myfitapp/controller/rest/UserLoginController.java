package itacad.aliaksandrkryvapust.myfitapp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


//@RestController
//@Validated
//@RequestMapping("/login")
//public class UserLoginController {
//    private final IUserManager menuItemManager;
//
//    @Autowired
//    public UserLoginController(IUserManager menuItemManager) {
//        this.menuItemManager = menuItemManager;
//    }
//
//    @GetMapping("/{name}")
//    protected ResponseEntity<UserDtoOutput> get(@PathVariable String name) {
//        return ResponseEntity.ok(menuItemManager.getUserByLogin(name));
//    }
//
//    @PostMapping
//    protected ResponseEntity<UserDtoOutput> registration(@RequestBody @Valid UserDtoInput dtoInput) {
//        return ResponseEntity.ok(this.menuItemManager.save(dtoInput));
//    }
//
//    @PutMapping("/{name}")
//    protected ResponseEntity<UserDtoOutput> put(@PathVariable String name, @Valid @RequestBody UserDtoInput dtoInput) {
//        return ResponseEntity.ok(this.menuItemManager.update(dtoInput, name));
//    }
//
//    @DeleteMapping("/{name}")
//    protected ResponseEntity<Object> delete(@PathVariable String name) {
//        menuItemManager.delete(name);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//}
