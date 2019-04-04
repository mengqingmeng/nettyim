package top.mengtech.nettyim.utils;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import top.mengtech.nettyim.attribute.Attributes;

public class LoginUtil {
    /**
     * 标记登陆成功
     * @param channel
     */
    public static void markAsLogin(Channel channel){
        channel.attr(Attributes.LOGIN).set(true);
    }

    /**
     * 是否登陆
     * @param channel
     * @return
     */
    public static boolean hasLogin(Channel channel){
        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);

        return loginAttr.get() !=null;
    }
}
