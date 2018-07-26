package au.herni.callcenter.model;


public abstract class Empleado {
	
	protected int idEmpleado;
	protected EstadoEmpleado estadoEmpleado = EstadoEmpleado.disponible;
	
	public Empleado(int id) {
		setIdEmpleado(id);
		System.out.println("empleado " + idEmpleado + ": " + estadoEmpleado);
	}
	
	public void attendCall( ) {
		System.out.println("empleado " + idEmpleado + " atiende llamada");
		setEstadoEmpleado(EstadoEmpleado.conLlamada);
	}
	public EstadoEmpleado getEstadoEmpleado() {
		return estadoEmpleado;
	}
	
	public void free() {
		System.out.println("empleado " + idEmpleado + " se libera");
		setEstadoEmpleado(EstadoEmpleado.disponible);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("empleado ");
		sb.append(this.getClass().getSimpleName()).append(" #").append(idEmpleado).append(": ").append(estadoEmpleado);
		return sb.toString();
	}
	public int getIdEmpleado() {
		return idEmpleado;
	}
	
	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	private void setEstadoEmpleado(EstadoEmpleado estadoEmpleado) {
		this.estadoEmpleado = estadoEmpleado;
	}
}