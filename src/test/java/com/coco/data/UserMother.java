package com.coco.data;

import com.coco.vo.UserVO;

public class UserMother {

    private static final UserVO TOM = new UserVO(-1, "tom", "smith", "tom@smith.com", "tom");

    public static UserVO aUser() {
        return TOM;
    }
}
