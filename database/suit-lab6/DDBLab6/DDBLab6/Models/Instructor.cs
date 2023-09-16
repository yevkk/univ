using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace DDBLab6.Models
{
    public class Instructor
    {
        [Column("id")]
        public int Id { get; set; }

        [Column("name")]
        public string? Name { get; set; }

        [Column("experience")]
        public int Experience { get; set; }

        [Column("certificate_number")]
        [Display(Name = "Certificate Number")]
        public string? CertificateNumber { get; set; }

        [Column("phone")]
        public string? Phone { get; set; }

        [ForeignKey("Car")]
        [Column("dedicated_car_id")]
        [Display(Name = "Dedicated Car")]
        public int? DedicatedCarId { get; set; }

        public Car? Car { get; set; }

        public string Desc => string.Format("{0}, {1}", Name, Phone);
    }
}
