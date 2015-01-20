package co.edu.uniandes.archimate.analysis;
import org.eclipse.swt.graphics.Image;

public interface ITableResult extends IResult{
	
	public Object[] toArray();
	
	public Image getImage(int index);
}
