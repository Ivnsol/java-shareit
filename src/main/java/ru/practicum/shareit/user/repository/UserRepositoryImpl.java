package ru.practicum.shareit.user.repository;

public class UserRepositoryImpl {
//
//    private UserRepository userRepository;
//
//
//    public List<User> findAll() {
//        return USERS;
//    }
//
//
//    public User get(Long userId) {
//        for (User user : USERS) {
//            if (user.getId().equals(userId)) {
//                return user;
//            }
//        }
//        throw new IllegalArgumentException("Incorrect id");
//    }
//
//
//    public User save(User user) {
//        for (User user1 : USERS) {
//            if (user1.getEmail().equals(user.getEmail())) {
//                throw new IllegalArgumentException("Such email is already exist");
//            }
//        }
//        user.setId(id);
//        id++;
//        USERS.add(user);
//        return user;
//    }
//
//
//    public void delete(Long id) throws IllegalAccessException {
//        Iterator<User> userIterator = USERS.listIterator();
//        while (userIterator.hasNext()) {
//            User user = userIterator.next();
//            if (user.getId().equals(id)) {
//                userIterator.remove();
//                return;
//            }
//        }
//        throw new IllegalAccessException("Incorrect id");
//    }
//
//
//    public User update(Long id, User updateUser) throws IllegalAccessException {
//        for (User user : USERS) {
//
//            if (user.getEmail().equals(updateUser.getEmail())) {
//                if (!user.getId().equals(id)) {
//                    throw new IllegalArgumentException("Such email is already exist");
//                } else {
//                    updateFieldsInUser(user, updateUser);
//                    return user;
//                }
//            }
//        }
//
//        for (User user2 : USERS) {
//
//            if (user2.getId().equals(id)) {
//                updateFieldsInUser(user2, updateUser);
//                return user2;
//            }
//        }
//        throw new IllegalAccessException("Incorrect id");
//    }
//
//    private void updateFieldsInUser(User user, User updateUser) {
//        if (updateUser.getEmail() != null) user.setEmail(updateUser.getEmail());
//        if (updateUser.getName() != null) user.setName(updateUser.getName());
//    }
}

