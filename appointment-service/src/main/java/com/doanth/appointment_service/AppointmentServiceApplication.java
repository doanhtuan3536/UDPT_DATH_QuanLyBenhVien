package com.doanth.appointment_service;

import com.doanth.appointment_service.dto.AppointmentInfoDTO;
import com.doanth.appointment_service.models.Appointment;
import com.doanth.appointment_service.models.Specialty;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppointmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentServiceApplication.class, args);
	}

	@Bean
	public ModelMapper getModelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

//		mapper.typeMap(AppointmentInfoDTO.class, Appointment.class).addMapping(
//			s -> s.getSpecialtyId(), (d, v) -> d.setSpecialty(new Specialty().setSpecialtyId((Integer) v));
//		);
//		mapper.typeMap(AppointmentInfoDTO.class, Appointment.class).addMappings(m ->
//				m.<Integer>map(AppointmentInfoDTO::getSpecialtyId,
//						(dest, v) -> {
//							System.out.println("mapping "+ v);
//							Specialty specialty = new Specialty();
//							specialty.setSpecialtyId(v);
//							dest.setSpecialty(specialty);
//						})
//		);

		Converter<Integer, Specialty> specialtyConverter = ctx -> {
			if (ctx.getSource() == null) return null;
			Specialty s = new Specialty();
			s.setSpecialtyId(ctx.getSource());
			return s;
		};

		mapper.typeMap(AppointmentInfoDTO.class, Appointment.class)
				.addMappings(m -> m.using(specialtyConverter)
						.map(AppointmentInfoDTO::getSpecialtyId, Appointment::setSpecialty));

		mapper.typeMap(Appointment.class, AppointmentInfoDTO.class).
				addMappings(m -> m.map(src -> src.getSpecialty().getSpecialtyId(), AppointmentInfoDTO::setSpecialtyId) );

		return mapper;
	}

}
