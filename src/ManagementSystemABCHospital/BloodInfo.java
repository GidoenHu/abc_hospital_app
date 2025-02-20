package ManagementSystemABCHospital;

public class BloodInfo {
    private String bloodType;  // A, B, AB, O
    private String rhFactor;   // +, -

    // Constructor
    public BloodInfo(String bloodType, String rhFactor) {
        this.bloodType = bloodType;
        this.rhFactor = rhFactor;
    }

    // Getters and setters
    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getRhFactor() {
        return rhFactor;
    }

    public void setRhFactor(String rhFactor) {
        this.rhFactor = rhFactor;
    }

    // For easy display of full blood type (e.g., "A+")
    @Override
    public String toString() {
        return bloodType + rhFactor;
    }
}