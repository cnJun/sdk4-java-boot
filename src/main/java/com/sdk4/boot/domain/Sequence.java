package com.sdk4.boot.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 序列号生成
 */
@Data
@Entity(name = "BootSequence")
@Table(name = "bcom_sequence")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "SEQ_nextval",
                procedureName = "_nextval",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "seqname", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "id", type = Long.class)
                })
})
public class Sequence {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid.hex", parameters = {
            @org.hibernate.annotations.Parameter(name = "type", value = "string") })
    private String id;
    private String name;
    private Long beginNum;
    private Integer incr;

}
