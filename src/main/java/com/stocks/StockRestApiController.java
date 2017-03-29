package com.stocks;

import java.lang.Iterable;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import com.util.CustomErrorType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/api")
public class StockRestApiController {

	public static final Logger logger = LoggerFactory
			.getLogger(StockRestApiController.class);

	@Autowired
	StockRepository stockRepository;

	@RequestMapping(value = "/get-random-number")
	public Integer getRandomNumber() {
		return (int) (Math.random() * 10);
	}

	// -------------------Retrieve All Stocks---------------------------------------------

	@RequestMapping(value = "/stocks", method = RequestMethod.GET)
	public ResponseEntity<List<Stock>> listAllStocks() {
		Iterator<Stock> stocks = stockRepository.findAll().iterator();
		if (!stocks.hasNext()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		List<Stock> stockList = new ArrayList<Stock>();
		while (stocks.hasNext()) {
			stockList.add(stocks.next());
		}
		return new ResponseEntity<List<Stock>>(stockList, HttpStatus.OK);
	}

	// -------------------Retrieve Single Stock------------------------------------------

	@RequestMapping(value = "/stocks/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getStock(@PathVariable("id") int id) {
		logger.info("Fetching Stock with id {}", id);
		Stock stock = stockRepository.findOne(id);
		if (stock == null) {
			logger.error("Stock with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Stock with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Stock>(stock, HttpStatus.OK);
	}

	// -------------------Create a Stock-------------------------------------------

	@RequestMapping(value = "/stocks", method = RequestMethod.POST)
	public ResponseEntity<?> createStock(@RequestBody Stock stock) {
		logger.info("Creating Stock : {}", stock);

		if (stockRepository.exists(stock.getId())) {
			logger.error(
					"Unable to create. A Stock with symbol {} already exist",
					stock.getSymbol());
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A Stock with symbol "
							+ stock.getSymbol() + " already exist."),
					HttpStatus.CONFLICT);
		}
		Stock saved = stockRepository.save(stock);

		return new ResponseEntity<Stock>(saved, HttpStatus.CREATED);
	}

	// ------------------- Upsert a Stock ------------------------------------------------

	@RequestMapping(value = "/stocks/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateStock(@PathVariable("id") int id,
			@RequestBody Stock stock) {
		logger.info("Updating Stock with id {}", id);

		Stock currentStock = stockRepository.findOne(id);

		if (currentStock == null) {
			currentStock = new Stock();
			currentStock.setId(id);
		}

		currentStock.setSymbol(stock.getSymbol());
		currentStock.setQuantity(stock.getQuantity());

		Stock saved = stockRepository.save(currentStock);
		return new ResponseEntity<Stock>(saved, HttpStatus.OK);
	}

	// ------------------- Delete a Stock-----------------------------------------
	@RequestMapping(value = "/stocks/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteStock(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting Stock with id {}", id);

		Stock stock = stockRepository.findOne(id);
		if (stock != null) {
			stockRepository.delete(id);
		}
		return new ResponseEntity<Integer>(id, HttpStatus.OK);
	}

}