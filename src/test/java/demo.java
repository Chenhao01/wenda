import java.io.Serializable;

/**
 * Created by 12274 on 2018/8/30.
 */
public class demo implements Serializable {
    private String name;
    private int age;
    private String sex;

    public demo(String name, int age, String sex, double selery) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.selery = selery;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public double getSelery() {
        return selery;
    }

    public void setSelery(double selery) {
        this.selery = selery;
    }

    private transient double selery;

}
