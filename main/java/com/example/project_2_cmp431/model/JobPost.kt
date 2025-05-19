package com.example.project_2_cmp431.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**  Job Post Entity   This entity is used to store job postings in the local database.

JSON data from the NYC Open Data API is mapped to this entity. */

@Entity
@Serializable
data class JobPost(

    @SerialName(value = "job_id")
    @PrimaryKey val jobId: Int, val agency: String,
    @SerialName(value = "number_of_positions") val numOfOpenPositions: String,
    @SerialName(value = "business_title") val businessTitle: String,
    @SerialName(value = "civil_service_title") val civilServiceTitle: String,
    @SerialName(value = "job_category") val jobCategory: String,
    @SerialName(value = "full_time_part_time_indicator") val fullOrPartTime: Char,
    @SerialName(value = "career_level") val careerLevel: String,
    @SerialName(value = "salary_range_from") val salaryRangeFrom: Double,
    @SerialName(value = "salary_range_to") val salaryRangeTo: Double,
    @SerialName(value = "salary_frequency") val salaryFrequency: String,
    @SerialName(value = "work_location") val agencyLocation: String,
    @SerialName(value = "division_work_unit") val divisionWorkUnit: String,
    @SerialName(value = "job_description") val jobDescription: String,
    @SerialName(value = "minimum_qual_requirements") val minRequirement: String = "",
    @SerialName(value = "preferred_skills") val preferredSkills: String = "",
    @SerialName(value = "additional_information") val additionalInfo: String = "",
    @SerialName(value = "to_apply") val toApply: String = "",
    @SerialName(value = "work_location_1") val workLocation: String = "",
    @SerialName(value = "posting_date") val postingDate: String,
    @SerialName(value = "post_until") val postUntil: String = "",
    @SerialName(value = "posting_updated") val postingLastUpdated: String,
    var isFavorite: Boolean = false,

)
