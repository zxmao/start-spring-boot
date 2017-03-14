package zxm.boot.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "users")
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer parentId;

    private String accountName;

    private String wxOpenId;

    private String wxUnionId;

    private String email;
    private String telephone;
    private String logo;
    private String userPassword;
    private String tmpPassword;
    private Date tmpPasswordExpiredDate;
    private String activationCode;
    private Integer userStatus;
    private String lastLoginSys;
    private Date createTime;
    private Integer authStatus;
    private String alias;



}