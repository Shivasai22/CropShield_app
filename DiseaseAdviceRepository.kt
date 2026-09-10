package com.cropshield.app

object DiseaseAdviceRepository {

    fun getAdvice(disease: String): DiseaseAdvice {

        val name = disease.lowercase()

        return when {

            name.contains("apple_scab") ->
                DiseaseAdvice(
                    disease = disease,
                    description =
                        "Apple scab is a fungal disease that commonly affects apple leaves and fruit.",
                    symptoms =
                        "Olive or dark spots may appear on leaves and fruit.",
                    recommendation =
                        "Remove affected leaves and fruit. Improve air circulation and avoid prolonged leaf wetness.",
                    prevention =
                        "Use resistant varieties, maintain orchard hygiene, and apply appropriate fungicide according to local guidance."
                )

            name.contains("black_rot") ->
                DiseaseAdvice(
                    disease = disease,
                    description =
                        "Black rot is a fungal disease affecting apple plants.",
                    symptoms =
                        "Dark leaf spots, fruit rot, and damaged branches may occur.",
                    recommendation =
                        "Remove infected plant material and improve sanitation around the plant.",
                    prevention =
                        "Remove fallen fruit and leaves and maintain good airflow."
                )

            name.contains("powdery_mildew") ->
                DiseaseAdvice(
                    disease = disease,
                    description =
                        "Powdery mildew is a fungal disease that produces a white powder-like growth.",
                    symptoms =
                        "White powdery patches can appear on leaves, stems, and young growth.",
                    recommendation =
                        "Remove severely affected leaves and improve air circulation.",
                    prevention =
                        "Avoid excessive humidity around foliage and maintain adequate spacing."
                )

            name.contains("early_blight") ->
                DiseaseAdvice(
                    disease = disease,
                    description =
                        "Early blight is a fungal disease commonly affecting tomato and potato plants.",
                    symptoms =
                        "Brown circular spots with concentric rings may appear on older leaves.",
                    recommendation =
                        "Remove affected leaves and avoid watering foliage directly.",
                    prevention =
                        "Use crop rotation, good spacing, sanitation, and appropriate disease management."
                )

            name.contains("late_blight") ->
                DiseaseAdvice(
                    disease = disease,
                    description =
                        "Late blight is a serious disease affecting potato and tomato plants.",
                    symptoms =
                        "Dark water-soaked lesions may develop on leaves and stems.",
                    recommendation =
                        "Remove severely affected plant material and isolate affected plants where possible.",
                    prevention =
                        "Maintain good airflow and avoid prolonged leaf moisture."
                )

            name.contains("bacterial_spot") ->
                DiseaseAdvice(
                    disease = disease,
                    description =
                        "Bacterial spot can affect several vegetable and fruit crops.",
                    symptoms =
                        "Small dark spots or lesions may develop on leaves and fruit.",
                    recommendation =
                        "Remove severely affected plant material and avoid overhead watering.",
                    prevention =
                        "Use clean planting material and maintain good field sanitation."
                )

            name.contains("leaf_mold") ->
                DiseaseAdvice(
                    disease = disease,
                    description =
                        "Tomato leaf mold is a fungal disease that develops under humid conditions.",
                    symptoms =
                        "Yellow areas may appear on the upper leaf surface with mold growth underneath.",
                    recommendation =
                        "Improve ventilation and reduce humidity around the leaves.",
                    prevention =
                        "Maintain adequate plant spacing and avoid excessive moisture."
                )

            name.contains("septoria") ->
                DiseaseAdvice(
                    disease = disease,
                    description =
                        "Septoria leaf spot is a fungal disease that affects tomato foliage.",
                    symptoms =
                        "Small circular spots with dark borders may appear on older leaves.",
                    recommendation =
                        "Remove affected leaves and keep foliage dry when possible.",
                    prevention =
                        "Use sanitation, crop rotation, and adequate spacing."
                )

            name.contains("spider_mites") ->
                DiseaseAdvice(
                    disease = disease,
                    description =
                        "Spider mites are small pests that can damage plant leaves.",
                    symptoms =
                        "Fine webbing, yellowing, and stippling can appear on leaves.",
                    recommendation =
                        "Inspect the underside of leaves and use an appropriate pest-management method.",
                    prevention =
                        "Monitor plants regularly and avoid conditions that encourage mite outbreaks."
                )

            name.contains("yellow_leaf_curl") ->
                DiseaseAdvice(
                    disease = disease,
                    description =
                        "Tomato yellow leaf curl virus is a viral disease commonly transmitted by whiteflies.",
                    symptoms =
                        "Leaf curling, yellowing, and reduced plant growth may occur.",
                    recommendation =
                        "Remove severely affected plants and manage insect vectors.",
                    prevention =
                        "Control whiteflies and use healthy planting material."
                )

            name.contains("mosaic_virus") ->
                DiseaseAdvice(
                    disease = disease,
                    description =
                        "Mosaic virus infections can cause characteristic patterns on plant leaves.",
                    symptoms =
                        "Mottled green and yellow patterns, leaf distortion, and reduced growth.",
                    recommendation =
                        "Remove severely infected plants and avoid spreading plant sap between plants.",
                    prevention =
                        "Use clean tools and healthy planting material."
                )

            name.contains("healthy") ->
                DiseaseAdvice(
                    disease = disease,
                    description =
                        "The model classified the plant as healthy.",
                    symptoms =
                        "No major disease pattern was detected by the model.",
                    recommendation =
                        "Continue normal plant care and monitor the plant regularly.",
                    prevention =
                        "Maintain good watering, nutrition, sanitation, and pest monitoring practices."
                )

            else ->
                DiseaseAdvice(
                    disease = disease,
                    description =
                        "The AI model detected this plant condition.",
                    symptoms =
                        "Review the captured image and monitor the plant for visible changes.",
                    recommendation =
                        "Inspect the plant carefully and consider appropriate agricultural guidance before treatment.",
                    prevention =
                        "Maintain good sanitation, plant spacing, watering practices, and regular monitoring."
                )
        }
    }
}