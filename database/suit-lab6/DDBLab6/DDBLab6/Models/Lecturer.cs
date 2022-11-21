using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace DDBLab6.Models
{
    public class Lecturer
    {
        [Column("id")]
        public int Id { get; set; }

        [Column("name")]
        public string? Name { get; set; }

        [Column("certificate_number")]
        [Display(Name = "Certificate Number")]
        public string? CertificateNumber { get; set; }

        [Column("phone")]
        public string? Phone { get; set; }
    }
}
