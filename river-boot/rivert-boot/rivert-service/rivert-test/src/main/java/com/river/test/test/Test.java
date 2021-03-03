package com.river.test.test;

import com.river.api.entity.system.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

public class Test {

    public static void main(String[] args) {

        SysUser u1= new SysUser();

        SysUser u2= new SysUser();

        System.out.println(u1.equals(u2));
        System.out.println(u1.hashCode());
        System.out.println(u2.hashCode());

        System.out.println(u1 == u2);

    }

}

@Data
@EqualsAndHashCode(callSuper = true)
class A extends B{
    private String name;
}

@Data
@EqualsAndHashCode(callSuper = false)
class B {
    private String id;
}

class C{

    private String name;
    private int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof C)) {
            return false;
        }
        C c = (C) o;
        return age == c.age &&
                name.equals(c.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}