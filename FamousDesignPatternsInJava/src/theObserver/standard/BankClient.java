package theObserver.standard;

import java.math.BigDecimal;
import java.util.Date;

public class BankClient implements Exchangeable, Observer {

	private Subject stockExchange;
	private DataNode pervious;
	
	public BankClient(Subject stockExchange) {
		this.stockExchange = stockExchange;
		stockExchange.registerObserver(this);
		
		pervious = new DataNode();
		pervious.setChangeRate(new BigDecimal(0.00))
		.setOperationDate(new Date());
	}
	
	@Override
	public void update(Object data) {
		
		final DataNode dataSet = (DataNode) data;
		final BigDecimal newValue = dataSet.getChangeRate();
		final BigDecimal oldValue = pervious.getChangeRate();
		
		if(newValue.compareTo(oldValue) == 1) {
			positive();
		} else if(newValue.compareTo(oldValue) == -1) {
			negative();
		} else {
			neutral();
		}

		pervious = dataSet;
	}

	@Override
	public void positive() {
		System.out.println("Stock went up...");

	}

	@Override
	public void negative() {
		System.out.println("Stock went down...");

	}

	@Override
	public void neutral() {
		System.out.println("Nothing changed.");

	}

}
