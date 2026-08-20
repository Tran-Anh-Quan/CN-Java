package vn.edu.eaut.lab8.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Tên đăng nhập không được để trống!")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống!")
    private String password;

    private boolean loggedIn = false;
    private String userFullName;

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();

        // Hardcoded authentication check for demonstration
        if (("admin".equalsIgnoreCase(username) && "123456".equals(password)) ||
            ("user".equalsIgnoreCase(username) && "123456".equals(password))) {

            this.loggedIn = true;
            this.userFullName = "admin".equalsIgnoreCase(username) ? "Quản Trị Viên (Admin)" : "Người Dùng Demo";

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Đăng nhập thành công!", "Chào mừng " + userFullName + " quay trở lại."));
            context.getExternalContext().getFlash().setKeepMessages(true);
            return "index?faces-redirect=true";
        }

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Đăng nhập thất bại!", "Tài khoản hoặc mật khẩu không chính xác (Thử: admin / 123456)."));
        return null;
    }

    public String logout() {
        this.loggedIn = false;
        this.username = null;
        this.password = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login?faces-redirect=true";
    }

    // Getters & Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isLoggedIn() { return loggedIn; }
    public boolean getLoggedIn() { return loggedIn; }
    public void setLoggedIn(boolean loggedIn) { this.loggedIn = loggedIn; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }
}
