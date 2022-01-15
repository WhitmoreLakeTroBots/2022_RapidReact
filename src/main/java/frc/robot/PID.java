package frc.robot;

public class PID {
	private double _Kp;
	private double _Ki;
	private double _Kd;
	
	private double pError = 0;
	private double iError = 0;
	private double dError = 0;
	private double deltaError = 0;
	private double cumError = 0;
	private double PIDValue = 0;
	private double lastError = 0;
	
	public PID (double Kp, double Ki, double Kd){
		_Kp = Kp;
		_Ki = Ki;
		_Kd = Kd;
	}
	
	public double calcError (double target, double current){
		return target - current;
	}
	
	public double calcP (double target, double current){
		double error = calcError(target, current);
		pError = _Kp * error;
		return pError;
	}
	
	public double calcP (double error){
		pError = _Kp * error;
		return pError;
	}
	
	public double calcI (double target, double current){
		double error = calcError(target, current);
		cumError = cumError + error;
		iError = _Ki * cumError;
		return iError;
	}
	
	public double calcI (double error){
		cumError = cumError + error;
		iError = _Ki * cumError;
		return iError;
	}
	
	public double calcD (double target, double current){
		double error = calcError(target, current);
		deltaError = error - lastError;
		lastError = error;
		dError = _Kd * deltaError;
		return dError;
	}
	
	public double calcD (double error){
		deltaError = error - lastError;
		lastError = error;
		dError = _Kd * deltaError;
		return dError;
	}
	
	public double getPError (){
		return pError;
	}
	
	public double getIError () {
		return iError;
	}
	
	public double getDError () {
		return dError;
	}
	
	public double calcPID (double target, double current){
		double error = calcError(target, current);
		double p = calcP(error);
		double i = calcI(error);
		double d = calcD(error);
		lastError = error;
		PIDValue = p + i + d; 
		return PIDValue;
	}
}