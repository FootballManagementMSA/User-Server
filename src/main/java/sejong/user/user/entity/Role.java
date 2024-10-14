package sejong.user.user.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    LEADER("동아리장"),MEMBER("동아리원");
    private String krState;
}
