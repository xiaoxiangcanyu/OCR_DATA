package JavaBean;

public class UserDO {
    private String name;
    private String gender;
    private boolean man;

    public UserDO() {
    }

    public UserDO(String name, String gender, boolean man) {
        this.name = name;
        this.gender = gender;
        this.man = man;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isMan() {
        return man;
    }

    public void setMan(boolean man) {
        this.man = man;
    }

    @Override
    public String toString() {
        return "UserDO{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", man=" + man +
                '}';
    }
}
