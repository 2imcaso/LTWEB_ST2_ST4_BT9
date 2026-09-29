package security.service.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.dto.UserDTO;
import security.mapper.UserMapper;
import security.repository.UserRepository;
import security.service.UserService;
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository users;
    private final UserMapper mapper;
    public UserServiceImpl(UserRepository users, UserMapper mapper) {
        this.users = users;
        this.mapper = mapper;
    }
    public UserDTO findById(Long id) {
        return mapper.toDto(users.findById(id).orElseThrow());
    }
}
