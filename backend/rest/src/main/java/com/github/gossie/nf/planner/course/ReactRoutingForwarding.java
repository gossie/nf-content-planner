package com.github.gossie.nf.planner.course;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Controller
public class ReactRoutingForwarding {

    @GetMapping(value = "/**/{[path:[^\\.]*}")
    public void forwardToRouteUrl(@RequestParam(required = false) String jwt, HttpServletResponse response) {
        if (jwt == null || "".equals(jwt)) {
            response.addCookie(new Cookie("jwt", jwt));
        }
    }
}
