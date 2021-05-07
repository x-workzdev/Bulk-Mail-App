package com.xworkz.bulk_mail_app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.google.gson.Gson;
import com.xworkz.bulk_mail_app.dto.MailChimpList;
import com.xworkz.bulk_mail_app.dto.SendMailDTO;
import com.xworkz.bulk_mail_app.service.OkHttpClientService;

@Controller
@RequestMapping("/")
public class MailController {

	@Autowired
	private OkHttpClientService clientService;

	private Logger logger = LoggerFactory.getLogger(MailController.class);

	public MailController() {
		logger.info("{} Is Created...........", this.getClass().getSimpleName());
	}
	
	@RequestMapping(value = "getListDetails.do", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public MailChimpList getList(@RequestHeader("Accept") HttpHeaders headers,
			@RequestParam("msgType") Integer msgType) {
		MailChimpList list = new Gson().fromJson(clientService.getAllMailChimpList(msgType), MailChimpList.class);
		logger.info("List is {}", list);
		return list;

	}

	@RequestMapping(value = "newsfeed.do", method = RequestMethod.POST)
	public ModelAndView sendNewsFeed(@ModelAttribute SendMailDTO mailDTO) {
		logger.info("Image Url Is {}", mailDTO.getImageURL());
		return sendMail(mailDTO);
	}

	@RequestMapping(value = "newJoiner.do", method = RequestMethod.POST)
	public ModelAndView sendNewJoiner(@ModelAttribute SendMailDTO mailDTO) {
		logger.info("data is {}", mailDTO);
		clientService.getHTMLTextFromFile(mailDTO);
		return sendMail(mailDTO);
	}

	@RequestMapping(value = "feesPaid.do", method = RequestMethod.POST)
	public ModelAndView sendFeesAcknnowledgement(@ModelAttribute SendMailDTO mailDTO) {
		logger.info("data is {}", mailDTO);
		return sendMail(mailDTO);
	}

	@RequestMapping(value = "birthdayGreeting.do", method = RequestMethod.POST)
	public ModelAndView sendBirthdayGreeting(@ModelAttribute SendMailDTO mailDTO) {
		logger.info("Image Url is {}", mailDTO.getImageURL());
		return sendMail(mailDTO);
	}

	@RequestMapping(value = "courseContain.do", method = RequestMethod.POST)
	public ModelAndView sendCourseContain(@ModelAttribute SendMailDTO mailDTO) {
		logger.info("data is {}", mailDTO);
		return sendMail(mailDTO);
	}

	public ModelAndView sendMail(SendMailDTO dto) {
		ModelAndView modelAndView = new ModelAndView("index");
		logger.info("List Name=" + dto.getListName());
		logger.info("Msg Id=" + dto.getMsgType());
		String listId = clientService.getListIdFromListName(dto.getListName(), dto.getMsgType());
		if (listId != null) {
			logger.info("list Id Is Fetched..........................");
			String campaignId = clientService.createCompaign(listId, dto);
			return campaignCreate(dto, modelAndView, campaignId);
		} else {
			modelAndView.addObject("msg", "List Name Was Not Found............");
			logger.error("List Name Was Not Found............");
			return modelAndView;
		}
	}

	private ModelAndView campaignCreate(SendMailDTO dto, ModelAndView modelAndView, String campaignId) {
		if (campaignId != null) {
			logger.info("Campaign Id Created Successfully...................");
			boolean edit = clientService.generateContent(campaignId, dto);
			return editMail(modelAndView, campaignId, edit, dto.getMsgType());
		} else {
			modelAndView.addObject("msg", "Campaign Is Not Created............");
			logger.error("Campaign Is Not Created............");
			return modelAndView;
		}
	}

	private ModelAndView editMail(ModelAndView modelAndView, String campaignId, boolean edit, Integer integer) {
		if (edit) {
			logger.info("Html Edited Successfully.........................");
			boolean result = clientService.sendCompaign(campaignId, integer);
			return send(modelAndView, result);
		} else {
			modelAndView.addObject("msg", "Campaign Is not Edited............");
			logger.error("Campaign Is not Edited...............");
			return modelAndView;
		}
	}

	private ModelAndView send(ModelAndView modelAndView, boolean result) {
		if (result) {
			modelAndView.addObject("msg", "Mail sent Successfully.............");
			logger.info("Mail sent Successfully...................");
			return modelAndView;
		} else {
			modelAndView.addObject("msg", "Mail sent faild, Please Try Again..........");
			logger.error("Mail Not Sent................");
			return modelAndView;
		}
	}
}