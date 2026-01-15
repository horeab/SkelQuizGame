package libgdx.game.model;

import java.util.Objects;

import libgdx.constants.user.AccountCreationSource;

public class BaseUserInfo {

    private int id;
    private String externalId;
    private AccountCreationSource accountCreationSource;
    private String fullName;
    private String profilePictureUrl;

    public BaseUserInfo(int id, String externalId, AccountCreationSource accountCreationSource, String fullName) {
        this.externalId = externalId;
        this.accountCreationSource = accountCreationSource;
        this.fullName = fullName;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public AccountCreationSource getAccountCreationSource() {
        return accountCreationSource;
    }

    public void setAccountCreationSource(AccountCreationSource accountCreationSource) {
        this.accountCreationSource = accountCreationSource;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseUserInfo that = (BaseUserInfo) o;
        return Objects.equals(externalId, that.externalId) &&
                accountCreationSource == that.accountCreationSource;
    }

    @Override
    public int hashCode() {

        return Objects.hash(externalId, accountCreationSource);
    }
}